package org.eclipse.edt.debug.internal.core.java.variables;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.edt.debug.core.EDTDebugCoreMessages;
import org.eclipse.edt.debug.core.java.IEGLJavaStackFrame;
import org.eclipse.edt.debug.core.java.IEGLJavaValue;
import org.eclipse.edt.debug.core.java.SMAPVariableInfo;
import org.eclipse.edt.debug.internal.core.java.EGLJavaValue;
import org.eclipse.edt.debug.internal.core.java.EGLJavaVariable;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

/**
 * Variable that just shows its value as toString(), with no children.
 */
public class ToStringVariable extends EGLJavaVariable
{
	
	public ToStringVariable( IDebugTarget target, IJavaVariable javaVariable, SMAPVariableInfo variableInfo, IEGLJavaStackFrame frame, IEGLJavaValue parent )
	{
		super( target, javaVariable, variableInfo, frame, parent );
	}
	
	@Override
	protected IEGLJavaValue createEGLValue( IJavaValue javaValue )
	{
		return new EGLJavaValue( getDebugTarget(), (IJavaValue)javaValue, this ) {
			@Override
			public String getValueString() throws DebugException
			{
				if ( javaValue.isNull() )
				{
					return "null"; //$NON-NLS-1$
				}
				
				if ( javaValue instanceof IJavaObject )
				{
					IJavaValue toStringValue;
					if ( "Ljava/lang/String;".equals( javaValue.getSignature() ) ) //$NON-NLS-1$
					{
						toStringValue = javaValue;
					}
					else
					{
						toStringValue = ((IJavaObject)javaValue).sendMessage( "toString", "()Ljava/lang/String;", null, getEGLStackFrame() //$NON-NLS-1$ //$NON-NLS-2$
								.getEGLThread().getJavaThread(), false );
					}
					
					if ( toStringValue == null )
					{
						return EDTDebugCoreMessages.ErrorRetrievingValue;
					}
					
					return toStringValue.getValueString();
				}
				
				return super.getValueString();
			}
			
			@Override
			public IVariable[] getVariables()
			{
				return new IVariable[ 0 ];
			}
			
			@Override
			public boolean hasVariables()
			{
				return false;
			}
		};
	}
}

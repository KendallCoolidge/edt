package org.eclipse.edt.compiler.internal.core.validation.annotation;

import org.eclipse.edt.compiler.binding.AS400DecFloatAnnotationTypeBinding;
import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;

public class AS400DecFloatValidator extends AS400DecimalValidator {
	
	@Override
	protected String getName() {
		return AS400DecFloatAnnotationTypeBinding.caseSensitiveName;
	}
	
	@Override
	protected String getInternedName() {
		return AS400DecFloatAnnotationTypeBinding.name;
	}
	
	protected void validateLengthAndDecimalsSpecified(IAnnotationBinding ann, Node errorNode, IProblemRequestor problemRequestor) {
		if (getLength(ann) == null) {
			problemRequestor.acceptProblem(errorNode, IProblemRequestor.AS400_PROPERTY_REQUIRED, new String[] {"length", getName()});
		}
	}

	protected void validateLengthAndDecimalsNotSpecified(IAnnotationBinding ann, Node errorNode, IProblemRequestor problemRequestor) {
		if (getLength(ann) != null) {
			problemRequestor.acceptProblem(errorNode, IProblemRequestor.AS400_PROPERTY_NOT_ALLOWED, new String[] {"length", getName()});
		}
	}
	
	protected void validateDecimals(IAnnotationBinding ann, Node errorNode, IProblemRequestor problemRequestor) {
		//do nothing
	}

	

}
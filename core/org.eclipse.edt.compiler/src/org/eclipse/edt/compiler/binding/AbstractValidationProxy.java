package org.eclipse.edt.compiler.binding;

import java.util.Collections;
import java.util.List;

import org.eclipse.edt.mof.egl.AnnotationType;

public abstract class AbstractValidationProxy implements IValidationProxy {
	
	private AnnotationType type;

	@Override
	public List<ValueValidationRule> getFieldValidators(String field) {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<FieldContentValidationRule> getPartSubTypeValidators() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<PartContentValidationRule> getPartTypeValidators() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<FieldAccessValidationRule> getFieldAccessValidators() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<InvocationValidationRule> getInvocationValidators() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public List<AnnotationValidationRule> getAnnotationValidators() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public AnnotationType getType() {
		return type;
	}
	
	@Override
	public void setType(AnnotationType type) {
		this.type = type;
	}
}
package com.jsfcompref.trainercomponents.validator;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validates that the current user has met the requirements for the event
 * 
 * @author vagner
 * 
 */
@FacesValidator(value = "com.jsfcompref.EventRequirements")
public class EventRequirementValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		Boolean hasMetRequirements = null;
		Boolean newHasMetRequirements = null;
		Application application = context.getApplication();
		
		ValueExpression ve = application.getExpressionFactory().
				createValueExpression(context.getELContext(), 
						"#{currentUser.status.qualified}", Boolean.class);
		
		try {
			/**
			 * This is an example of programmatically invoking a ValueExpression. It
			 * is not used in the validator calculation, but it is left in for
			 * illustration.
			 */
			hasMetRequirements = (Boolean) ve.getValue(context.getELContext());
			newHasMetRequirements = (Boolean) value;
		} catch (Throwable e) {
			// log error
		}
		
		if (!newHasMetRequirements.booleanValue()) {
			FacesMessage message = new FacesMessage("You still have more work to do!");
			throw new ValidatorException(message);
		}
	}

}

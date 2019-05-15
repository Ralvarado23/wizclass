package com.wizclass.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This class indicates the constraints used in the validation of Telefono variable.
 * @author Raul Alvarado
 *
 */
public class TelefonoValidator implements ConstraintValidator<Telefono, String>{

	@Override
	public void initialize(Telefono telefono) {
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && value.matches("^(6|7|9)[0-9]{8}");
	}
}
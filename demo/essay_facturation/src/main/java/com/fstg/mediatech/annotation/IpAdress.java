package com.fstg.mediatech.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IpAdressValidation.class})
public @interface IpAdress  {
	
	String message() default "IpAddress invalid";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}

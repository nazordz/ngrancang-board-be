package com.unindra.ngrancang.utility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class) // specify the validator class
public @interface ValidImageFile {
    String message() default "Invalid file type. Only image files are allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

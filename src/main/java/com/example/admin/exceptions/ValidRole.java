package com.example.admin.exceptions;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "Role ID must be either 1 or 2";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

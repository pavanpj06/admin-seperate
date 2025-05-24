package com.example.admin.exceptions;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlanDatesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPlanDates {
	String message() default "End date should be at least 6 months after start date.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

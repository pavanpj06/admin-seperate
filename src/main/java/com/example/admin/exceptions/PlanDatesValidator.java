package com.example.admin.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.example.admin.bindings.PlanForm;

public class PlanDatesValidator implements ConstraintValidator<ValidPlanDates, PlanForm> {

    @Override
    public boolean isValid(PlanForm form, ConstraintValidatorContext context) {
        if (form.getStartDate() == null || form.getEndDate() == null) {
            return true; // Let @NotNull handle these
        }

        LocalDate expectedEndDate = form.getStartDate().plusMonths(6);
        boolean isValid = form.getEndDate().isAfter(expectedEndDate) || form.getEndDate().isEqual(expectedEndDate);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date must be at least 6 months after start date.")
                   .addPropertyNode("endDate") // This is important to show on the correct field
                   .addConstraintViolation();
        }

        return isValid;
    }
}

package com.example.admin.exceptions;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotPastDateValidator implements ConstraintValidator<NotPastDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true; // Let @NotNull handle null
        return !value.isBefore(LocalDate.now());
    }
}

package com.deftdevs.bootstrapi.commons.util;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelValidationUtilTest {

    @Test
    void testThrowNoValidationException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        assertDoesNotThrow(() -> {
            ModelValidationUtil.processValidationResult(violations);
        });
    }

    @Test
    void testThrowValidationException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        violations.add(ConstraintViolationImpl.forBeanValidation("", null, null, "",
                null, null, null, null, null, null, null));

        assertThrows(ValidationException.class, () -> {
            ModelValidationUtil.processValidationResult(violations);
        });
    }
}

package de.aservo.confapi.commons.util;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanValidationUtilTest {

    @Test
    void testThrowNoValidationException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        assertDoesNotThrow(() -> {
            BeanValidationUtil.processValidationResult(violations);
        });
    }

    @Test
    void testThrowValidationException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        violations.add(ConstraintViolationImpl.forBeanValidation("", null, null, "",
                null, null, null, null, null, null, null));

        assertThrows(ValidationException.class, () -> {
            BeanValidationUtil.processValidationResult(violations);
        });
    }
}

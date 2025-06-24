package com.deftdevs.bootstrapi.commons.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Model validation service.
 */
public class ModelValidationUtil {

    /**
     * Validates the given bean using jakarta.validation impl from hibernate reference.
     *
     * @param bean the bean
     */
    public static void validate(Object bean) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        processValidationResult(validator.validate(bean));
    }

    /**
     * Processes validation violations by throwing a ValidationException including the violation texts
     *
     * @param violations validation violations
     */
    public static void processValidationResult(Set<ConstraintViolation<Object>> violations) {
        if (!violations.isEmpty()) {
            List<String> collect = violations.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList());
            throw new ValidationException(String.join("\n", collect));
        }
    }

    private ModelValidationUtil() {
    }

}

package com.deftdevs.bootstrapi.commons.util;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

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

    // the provider is selected explicitly because the products do not expose a Jakarta Bean Validation
    // provider to plugin bundles; the parameter message interpolator avoids a runtime EL dependency
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.byProvider(HibernateValidator.class)
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    /**
     * Validates the given bean using jakarta.validation impl from hibernate reference.
     *
     * @param bean the bean
     */
    public static void validate(Object bean) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
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

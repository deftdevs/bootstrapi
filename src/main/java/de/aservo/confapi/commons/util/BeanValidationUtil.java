package de.aservo.confapi.commons.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Bean validation service.
 */
public class BeanValidationUtil {

    /**
     * Validates the given bean using javax.validation impl from hibernate reference.
     *
     * @param bean the bean
     */
    public static void validate(Object bean) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            List<String> collect = violations.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList());
            throw new ValidationException(String.join("\n", collect));
        }
    }

    private BeanValidationUtil() {
    }

}

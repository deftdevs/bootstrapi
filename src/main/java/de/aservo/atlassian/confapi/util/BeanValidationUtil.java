package de.aservo.atlassian.confapi.util;

import de.aservo.atlassian.confapi.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.*;
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
     * @throws BadRequestException the bad request exception
     */
    public static void validate(Object bean) throws BadRequestException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            List<String> collect = violations.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.toList());
            throw new BadRequestException(StringUtils.join(collect, "\n"));
        }
    }

    private BeanValidationUtil() {
    }

}

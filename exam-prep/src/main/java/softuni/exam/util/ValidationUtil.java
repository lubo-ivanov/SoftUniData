package softuni.exam.util;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;

public interface ValidationUtil {

    <E> boolean isValid(E entity);
}

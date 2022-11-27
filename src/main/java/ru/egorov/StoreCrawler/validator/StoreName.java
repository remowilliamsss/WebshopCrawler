package ru.egorov.StoreCrawler.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StoreValidator.class)
@Documented
public @interface StoreName {

    String message() default "The store with this name is not supported";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

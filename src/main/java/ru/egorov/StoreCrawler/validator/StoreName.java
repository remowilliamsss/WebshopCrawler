package ru.egorov.StoreCrawler.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StoreValidator.class)
@Documented
// TODO: 13.12.2022 идея прикольная, но дикое извращение.
//  Что мешает просто передавать значение енама параметром контроллера? Request param точно смапит,
//  насчет Path var не помню, надо проверять
public @interface StoreName {

    String message() default "The store with this name is not supported";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

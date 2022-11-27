package ru.egorov.StoreCrawler.validator;

import ru.egorov.StoreCrawler.Store;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class StoreValidator implements ConstraintValidator<StoreName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(Store.values())
                .anyMatch(store -> store.getName().equals(value));
    }
}

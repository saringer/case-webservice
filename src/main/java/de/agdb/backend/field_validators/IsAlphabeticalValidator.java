package de.agdb.backend.field_validators;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

/**
 * https://stackoverflow.com/questions/16106260/thread-safe-singleton-class
 */
public class IsAlphabeticalValidator implements Validator<String>{


    @Override
    public ValidationResult apply(String value, ValueContext valueContext) {
        if(value.matches("(?U)[\\p{L}\\p{M}\\s'-]+")) {
            return ValidationResult.ok();
        } else {
            return ValidationResult.error(
                    "Title must begin with a letter");
        }
    }


}

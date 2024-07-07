package com.labhesh.Todos.Todos.validation;



import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampValidatorImpl implements ConstraintValidator<ValidTimestamp, String> {
    private boolean isFuture;

    @Override
    public void initialize(ValidTimestamp constraintAnnotation) {
        this.isFuture = constraintAnnotation.isFuture();
    }

    @Override
    public boolean isValid(String timestamp, ConstraintValidatorContext context) {
        if (timestamp == null || timestamp.isEmpty()) {
            return true;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(timestamp);
            if (isFuture) {
                return parsedDate.after(new Date());
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
    }
}

package com.example.subscription.service;

import com.example.subscription.model.DateSortCriteria;
import com.example.subscription.model.StringSortCriteria;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.model.exceptions.InvalidRequestException;
import com.example.subscription.util.GeneralValidationUtil;
import com.example.subscription.util.ValidationProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationService {
    @NonNull
    private final ValidationProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);
    private final List<String> errors = new ArrayList<>();

    public void validateSubscription(SubscriptionModel subscription) throws InvalidRequestException {
        errors.clear();
        validateEmail(subscription.getEmail());
        validateDate(subscription.getDate());
        if (!errors.isEmpty())
            throw new InvalidRequestException(errors);
    }

    public void validateFilterDates(DateSortCriteria dateSortCriteria) throws InvalidRequestException {
        errors.clear();
        String beforeString = dateSortCriteria.getBeforeDate();
        String afterString = dateSortCriteria.getAfterDate();
        LocalDate beforeDate, afterDate;
        beforeDate = afterDate = null;
        if (beforeString != null) {
            beforeDate = validateDate(beforeString);
        }
        if (afterString != null) {
            afterDate = validateDate(afterString);
        }
        if (beforeDate != null && afterDate != null) {
            compareDates(beforeDate, afterDate);
        }
        if (!errors.isEmpty())
            throw new InvalidRequestException(errors);
    }

    public void validateFilterStrings(StringSortCriteria stringSortCriteria) throws InvalidRequestException {
        errors.clear();
        if (stringSortCriteria.getStartsWith() != null && stringSortCriteria.getStartsWith().isEmpty()) {
            errors.add(properties.getErrorInvalidFilterString().formatted("startsWith"));
        }
        if (stringSortCriteria.getEndsWith() != null && stringSortCriteria.getEndsWith().isEmpty()) {
            errors.add(properties.getErrorInvalidFilterString().formatted("endsWith"));
        }
        if (!errors.isEmpty())
            throw new InvalidRequestException(errors);
    }

    public List<String> getErrorMessages() {
        return errors;
    }

    private void validateEmail(String email) {
        if (!GeneralValidationUtil.patternMatches(email, properties.getEmailRegex())) {
            errors.add(properties.getErrorInvalidEmail().formatted(email));
        }
    }

    private LocalDate validateDate(String dateString) {
        LocalDate date = GeneralValidationUtil.getValidDate(dateString, properties.getDateFormat());
        if (date == null) {
            // check date has a valid format
            errors.add(properties.getErrorInvalidDate().formatted(dateString, properties.getDateFormat()));
        } else {
            // check before current date
            if (date.isAfter(LocalDate.now())) {
                errors.add(properties.getErrorDateIsAfterCurrentDate().formatted(dateString));
            }
        }
        return date;
    }

    private void compareDates(LocalDate beforeDate, LocalDate afterDate) {
        if (beforeDate.isAfter(afterDate)) {
            // check afterDate > beforeDate
            errors.add(properties.getErrorBeforeIsMoreThanAfterDate()
                    .formatted(beforeDate.toString(), afterDate.toString()));
        }
    }

}

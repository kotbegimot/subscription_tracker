package com.example.subscription.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalDate;
import java.time.chrono.IsoEra;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.regex.Pattern;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralValidationUtil {
    private final static Logger logger = LoggerFactory.getLogger(GeneralValidationUtil.class);

    /**
     * Checks that input date matches with input date format
     *
     * @param dateString - input date (string)
     * @param dateFormat - input date format (dateTime)
     * @return bool
     */
    public static LocalDate getValidDate(String dateString, String dateFormat) {
        LocalDate date = null;
        if (!dateString.isEmpty()) {
            try {
                DateTimeFormatter f = new DateTimeFormatterBuilder()
                        .appendPattern(dateFormat)
                        .parseDefaulting(ChronoField.ERA, IsoEra.CE.getValue())
                        .toFormatter()
                        .withResolverStyle(ResolverStyle.STRICT);
                date = LocalDate.parse(dateString, f);
            } catch (DateTimeParseException e) {
                logger.error("Date validation error: {}, date: {}, date format: {}", e.getMessage(), dateString, dateFormat);
            } catch (Exception e) {
                logger.error("DateTimeFormatterBuilder error: {}, date format: {}", e.getMessage(), dateFormat);
            }
        }
        return date;
    }

    /**
     * Checks that a given string matches a given pattern
     *
     * @param matchingString - checking string
     * @param regexPattern   - pattern for checking
     * @return bool
     */
    public static boolean patternMatches(String matchingString, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(matchingString)
                .matches();
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static boolean isValidEmailApache(String email) {
        return EmailValidator.getInstance(true).isValid(email);
    }
}

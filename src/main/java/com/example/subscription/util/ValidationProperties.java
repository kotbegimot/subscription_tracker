package com.example.subscription.util;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Validation properties for subscriptions
 */
@Configuration
@ConfigurationProperties(prefix = "validation")
@Getter
@Setter
@Validated
public class ValidationProperties {
    @NotNull(message = "Validation property \"dateFormat\" cannot be null: check application.yml")
    String dateFormat;
    @NotNull(message = "Validation property \"exceptionDateFormat\" cannot be null: check application.yml")
    String exceptionDateFormat;
    @NotEmpty(message = "Validation property \"dateFormatList\" cannot be null: check application.yml")
    List<String> dateFormatList;
    @NotNull(message = "Validation property \"emailRegex\" cannot be null: check application.yml")
    String emailRegex;
    @NotNull(message = "Validation property \"errorInvalidEmail\" cannot be null: check application.yml")
    String errorInvalidEmail;
    @NotNull(message = "Validation property \"errorInvalidDate\" cannot be null: check application.yml")
    String errorInvalidDate;
    @NotNull(message = "Validation property \"errorDateIsAfterCurrentDate\" cannot be null: check application.yml")
    String errorDateIsAfterCurrentDate;
    @NotNull(message = "Validation property \"errorBeforeIsMoreThanAfterDate\" cannot be null: check application.yml")
    String errorBeforeIsMoreThanAfterDate;
}

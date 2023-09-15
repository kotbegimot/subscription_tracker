package com.example.subscription.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchSubscriptionFoundException extends RuntimeException {
    public NoSuchSubscriptionFoundException(String email) {
        super("Subscription email is not found: " + email);
    }
}

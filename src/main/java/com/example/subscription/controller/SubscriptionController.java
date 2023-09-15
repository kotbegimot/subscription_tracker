package com.example.subscription.controller;

import com.example.subscription.model.DateSortCriteria;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.service.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionsService service;

    /**
     * GET request for getting a list of all the subscriptions
     * * Support optional filters by fields:
     * * - beforeDate
     * * - afterDate
     *
     * @param dateSortCriteria - contains filter values: beforeDate and afterDate as strings
     * @return - list of subscriptions objects in JSON format
     */
    @GetMapping()
    public List<SubscriptionModel> getSubscriptions(DateSortCriteria dateSortCriteria) {
        return service.getSubscriptions(dateSortCriteria);
    }

    /**
     * GET request that returns subscription by email.
     *
     * @param email - email string
     * @return subscription object
     */
    @GetMapping("/{email}")
    public SubscriptionModel getSubscription(@PathVariable String email) {
        return service.getSubscription(email);
    }

    /**
     * Creates new subscription entity
     *
     * @param newSubscription - subscription object
     */
    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createSubscription(@RequestBody SubscriptionModel newSubscription) {
        service.createSubscription(newSubscription);
    }

    /**
     * DELETE request that removes subscription by email string.
     *
     * @param email - email string
     */
    @DeleteMapping("/{email}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteSubscription(@PathVariable String email) {
        service.deleteSubscription(email);
    }
}

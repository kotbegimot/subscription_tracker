package com.example.subscription.repository;

import com.example.subscription.entity.SubscriptionEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepository {
    @NonNull
    private final SubscriptionRepositoryJPA repositoryJPA;

    public List<SubscriptionEntity> getAllSubscriptions() {
        return repositoryJPA.findAll();
    }

    public Optional<SubscriptionEntity> getSubscriptionByEmail(String email) {
        return Optional.ofNullable(repositoryJPA.findByEmail(email));
    }

    public void createSubscription(SubscriptionEntity subscription) {
        repositoryJPA.save(subscription);
    }

    public void deleteSubscription(String email) {
        SubscriptionEntity entity = repositoryJPA.findByEmail(email);
        repositoryJPA.delete(entity);
    }

    public List<SubscriptionEntity> getSubscriptionsBefore(LocalDate beforeDate) {
        return repositoryJPA.findAllByDateBefore(beforeDate);
    }

    public List<SubscriptionEntity> getSubscriptionsAfter(LocalDate afterDate) {
        return repositoryJPA.findAllByDateAfter(afterDate);
    }

    public List<SubscriptionEntity> getSubscriptionsAfterAndBefore(LocalDate beforeDate, LocalDate afterDate) {
        return repositoryJPA.findAllByDateLessThanAndDateGreaterThan(beforeDate, afterDate);
    }

    public List<SubscriptionEntity> getSubscriptionsStartsWith(String startsWith) {
        return repositoryJPA.findAllByEmailStartsWith(startsWith);
    }

    public List<SubscriptionEntity> getSubscriptionsEndsWith(String endsWith) {
        return repositoryJPA.findAllByEmailEndsWith(endsWith);
    }
}

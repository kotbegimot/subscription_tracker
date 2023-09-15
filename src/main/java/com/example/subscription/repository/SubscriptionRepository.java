package com.example.subscription.repository;

import com.example.subscription.entity.SubscriptionEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepository {
    @NonNull
    private final SubscriptionRepositoryJPA repositoryJPA;

    public List<SubscriptionEntity> getAllSubscriptions() {
        return repositoryJPA.findAll();
    }

    public SubscriptionEntity getSubscriptionByEmail(String email) {
        return repositoryJPA.findByEmail(email);
    }

    @Transactional
    public void createSubscription(SubscriptionEntity subscription) {
        repositoryJPA.save(subscription);
    }

    @Transactional
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
}

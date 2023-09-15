package com.example.subscription.repository;

import com.example.subscription.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepositoryJPA extends JpaRepository<SubscriptionEntity, Integer> {
    List<SubscriptionEntity> findAllByDateLessThanAndDateGreaterThan(LocalDate dateAfter, LocalDate dateBefore);

    List<SubscriptionEntity> findAllByDateAfter(LocalDate dateAfter);

    List<SubscriptionEntity> findAllByDateBefore(LocalDate dateBefore);

    SubscriptionEntity findByEmail(String email);
}

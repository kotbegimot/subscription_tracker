package com.example.subscription.service;

import com.example.subscription.entity.SubscriptionEntity;
import com.example.subscription.model.DateSortCriteria;
import com.example.subscription.model.StringSortCriteria;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.model.exceptions.NoSuchSubscriptionFoundException;
import com.example.subscription.model.exceptions.UserAlreadyExistsException;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.util.ValidationProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionsService {
    @NonNull
    private final SubscriptionRepository repository;
    @NonNull
    private final ValidationProperties properties;
    @NonNull
    private final SubscriptionMapper mapper;
    @NonNull
    private final ValidationService validationService;

    public List<SubscriptionModel> getSubscriptions(DateSortCriteria dateSortCriteria,
                                                    StringSortCriteria stringSortCriteria) {
        if (dateSortCriteria.getBeforeDate() == null && dateSortCriteria.getAfterDate() == null &&
                stringSortCriteria.getStartsWith() == null && stringSortCriteria.getEndsWith() == null) {
            return getAllSubscriptions();
        } else {
            validationService.validateFilterDates(dateSortCriteria);
            validationService.validateFilterStrings(stringSortCriteria);
            if (dateSortCriteria.getBeforeDate() == null) {
                return mapper.toModels(repository.getSubscriptionsAfter(LocalDate.parse(dateSortCriteria.getAfterDate(),
                        DateTimeFormatter.ofPattern(properties.getDateFormat()))));
            } else if (dateSortCriteria.getAfterDate() == null) {
                return mapper.toModels(repository.getSubscriptionsBefore(LocalDate.parse(dateSortCriteria.getBeforeDate(),
                        DateTimeFormatter.ofPattern(properties.getDateFormat()))));
            } else {
                return mapper.toModels(repository.getSubscriptionsAfterAndBefore(
                        LocalDate.parse(dateSortCriteria.getBeforeDate(),
                                DateTimeFormatter.ofPattern(properties.getDateFormat())),
                        LocalDate.parse(dateSortCriteria.getAfterDate(),
                                DateTimeFormatter.ofPattern(properties.getDateFormat()))));
            }
        }
    }

    public List<SubscriptionModel> getAllSubscriptions() {
        return mapper.toModels(repository.getAllSubscriptions());
    }

    public SubscriptionModel getSubscription(String email) {
        SubscriptionEntity entity = repository.getSubscriptionByEmail(email)
                .orElseThrow(() -> new NoSuchSubscriptionFoundException(email));
        return mapper.toModel(entity);
    }

    @Transactional
    public void createSubscription(SubscriptionModel newSubscription) {
        if (repository.getSubscriptionByEmail(newSubscription.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(newSubscription.getEmail());
        }
        validationService.validateSubscription(newSubscription);
        repository.createSubscription(mapper.toEntity(newSubscription));
    }

    @Transactional
    public void deleteSubscription(String email) {
        repository.getSubscriptionByEmail(email).orElseThrow(() -> new NoSuchSubscriptionFoundException(email));
        repository.deleteSubscription(email);
    }
}

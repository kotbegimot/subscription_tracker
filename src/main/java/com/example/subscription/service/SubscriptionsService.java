package com.example.subscription.service;

import com.example.subscription.entity.SubscriptionEntity;
import com.example.subscription.model.DateSortCriteria;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.model.exceptions.NoSuchSubscriptionFoundException;
import com.example.subscription.model.exceptions.UserAlreadyExistsException;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.util.ValidationProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionsService {
    @NonNull private final SubscriptionRepository repository;
    @NonNull private final ValidationProperties properties;
    @NonNull private final SubscriptionMapper mapper;
    @NonNull private final ValidationService validationService;

    public List<SubscriptionModel> getSubscriptions(DateSortCriteria dateSortCriteria) {
        if (dateSortCriteria.getBeforeDate() == null && dateSortCriteria.getAfterDate() == null) {
            return getAllSubscriptions();
        } else {
            validationService.validateFilterDates(dateSortCriteria);
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
        SubscriptionEntity entity = checkSubscriptionEntityByEmail(email);
        return mapper.toModel(entity);
    }

    public void createSubscription(SubscriptionModel newSubscription) {
        validationService.validateSubscription(newSubscription);
        checkExistenceSubscriptionEntityByEmail(newSubscription.getEmail());
        repository.createSubscription(mapper.toEntity(newSubscription));
    }

    public void deleteSubscription(String email) {
        checkSubscriptionEntityByEmail(email);
        repository.deleteSubscription(email);
    }

    private SubscriptionEntity checkSubscriptionEntityByEmail(String email) throws NoSuchSubscriptionFoundException {
        SubscriptionEntity entity = repository.getSubscriptionByEmail(email);
        if (entity == null) {
            throw new NoSuchSubscriptionFoundException(email);
        }
        return entity;
    }

    private void checkExistenceSubscriptionEntityByEmail(String email) throws UserAlreadyExistsException {
        SubscriptionEntity entity = repository.getSubscriptionByEmail(email);
        if (entity != null) {
            throw new UserAlreadyExistsException(email);
        }
    }
}

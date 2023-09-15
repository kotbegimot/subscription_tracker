package com.example.subscription.service;

import com.example.subscription.entity.SubscriptionEntity;
import com.example.subscription.model.DateSortCriteria;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.model.exceptions.NoSuchSubscriptionFoundException;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.util.ValidationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SubscriptionsServiceTest {
    private final SubscriptionRepository repository = mock(SubscriptionRepository.class);
    private final ValidationProperties properties = mock(ValidationProperties.class);
    private final SubscriptionMapper mapper = mock(SubscriptionMapper.class);
    private final ValidationService validationService = mock(ValidationService.class);

    private SubscriptionsService service;
    private SubscriptionEntity entity;
    private SubscriptionModel model;
    private List<SubscriptionEntity> entityList;
    private List<SubscriptionModel> modelList;

    @BeforeEach
    void setup() {
        service = new SubscriptionsService(repository, properties, mapper, validationService);
        entity = SubscriptionEntity.builder()
                .id(0)
                .email("king_james@nba.com")
                .date(LocalDate.parse("2018-08-11"))
                .build();
        model = SubscriptionModel.builder()
                .email("king_james@nba.com")
                .date("11.08.2018")
                .build();
        entityList = List.of(entity);
        modelList = List.of(model);
    }

    @Test
    @DisplayName("Service should return list of Subscription objects")
    void getAllSubscriptionsTest() {
        DateSortCriteria dateSortCriteria = DateSortCriteria.builder()
                .afterDate(null)
                .beforeDate(null)
                .build();
        when(repository.getAllSubscriptions()).thenReturn(entityList);
        when(mapper.toModels(entityList)).thenReturn(modelList);

        assertEquals(modelList, service.getSubscriptions(dateSortCriteria));
        verify(repository, times(1)).getAllSubscriptions();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Service should throw the NoSuchSubscriptionFoundException exception")
    void getInvalidSubscriptionTest() {
        String email = "goat@nma.com";
        when(repository.getSubscriptionByEmail(email)).thenReturn(null);

        Assertions.assertThrows(NoSuchSubscriptionFoundException.class, () -> service.getSubscription(email));

        verify(repository, times(1)).getSubscriptionByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Service should return Subscription object")
    void getExistingSubscriptionTest() {
        String email = "king_james@nba.com";
        when(repository.getSubscriptionByEmail(email)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);

        assertEquals(model, service.getSubscription(email));
        verify(repository, times(1)).getSubscriptionByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Service should execute subscription creation")
    void createSubscriptionExecutedTest() {
        doNothing().when(validationService).validateSubscription(model);
        when(repository.getSubscriptionByEmail(model.getEmail())).thenReturn(null);
        when(mapper.toEntity(model)).thenReturn(entity);

        service.createSubscription(model);

        ArgumentCaptor<SubscriptionEntity> captor = ArgumentCaptor.forClass(SubscriptionEntity.class);
        verify(repository, times(1)).getSubscriptionByEmail(model.getEmail());
        verify(mapper, times(1)).toEntity(model);
        verify(repository, times(1)).createSubscription(captor.capture());
        assertEquals(captor.getValue().getEmail(), model.getEmail());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Service should execute subscription deletion")
    void deleteSubscriptionExecutedTest() {
        String email = "king_james@nba.com";
        when(repository.getSubscriptionByEmail(email)).thenReturn(entity);

        service.deleteSubscription(email);

        verify(repository, times(1)).getSubscriptionByEmail(email);
        verify(repository, times(1)).deleteSubscription(email);
        verifyNoMoreInteractions(repository);
    }
}
package com.example.subscription.service;

import com.example.subscription.entity.SubscriptionEntity;
import com.example.subscription.model.SubscriptionModel;
import com.example.subscription.util.ValidationProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionMapper {
    @NonNull
    private final ValidationProperties properties;

    public SubscriptionModel toModel(SubscriptionEntity entity) {
        return SubscriptionModel.builder()
                .email(entity.getEmail())
                .date(entity.getDate().toString())
                .build();
    }

    public List<SubscriptionModel> toModels(List<SubscriptionEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .toList();
    }

    public SubscriptionEntity toEntity(SubscriptionModel model) {
        return SubscriptionEntity.builder()
                .id(0)
                .email(model.getEmail())
                .date(LocalDate.parse(model.getDate(), DateTimeFormatter.ofPattern(properties.getDateFormat())))
                .build();
    }
}

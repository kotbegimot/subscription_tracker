package com.example.subscription.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@AllArgsConstructor
@Builder()
public class SubscriptionModel {
    private String email;
    private String date;
}

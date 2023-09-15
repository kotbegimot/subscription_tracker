package com.example.subscription.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateSortCriteria {
    private String beforeDate;
    private String afterDate;
}

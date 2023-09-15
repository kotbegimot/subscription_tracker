package com.example.subscription.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponseModel {
    private Integer status;
    private String error;
    private List<String> messages;
    private String path;
    private String timestamp;
    private String controllerName;
    private String serviceName;
    private String projectName;
}

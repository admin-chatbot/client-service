package com.voicebot.commondcenter.clientservice.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "servicelog")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLog implements BaseEntity, Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "service_log_sequence";

    @Id
    private Long id;

    @NotBlank( message = "Service Endpoint should not null or empty.")
    private String serviceEndpoint;

    @NotBlank( message = "Service Name should not null or empty.")
    private String serviceName;

    private List<ServiceParameter> parameters;

    @NotNull( message = "Name should not null or empty.")
    private Long client;

    @NotNull( message = "Application Id should not null or empty.")
    private Long application;

    private Date logDate;

    private String response;



}
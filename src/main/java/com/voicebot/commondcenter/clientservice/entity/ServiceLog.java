package com.voicebot.commondcenter.clientservice.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "servicelog")
@Data
@Builder
public class ServiceLog {

    @Transient
    public static final String SEQUENCE_NAME = "service_log_sequence";

    @Id
    private Long id;

    private String serviceEndpoint;

    private String serviceName;

    private List<ServiceParameter> parameters;

    private Long client;

    private Long application;

    private Date logDate;

    private String response;



}

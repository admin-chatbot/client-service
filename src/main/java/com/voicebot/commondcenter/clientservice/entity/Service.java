package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.discovery.service.model.Authorization;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "service")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Service implements BaseEntity, Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "service_sequence";

    @Id
    private Long id;

    @NotBlank( message = "Endpoint should not null or empty.")
    private String endpoint;

    private String keyword;

    @NotBlank( message = "Method should not null or empty.")
    private String method; //GET/POST/PUT

    @NotBlank( message = "Name should not null or empty.")
    private String name;

    private String summary;

    private Authorization authorization;

    @NotNull( message = "Response Type should not null.")
    private List<String> responseType;

    private List<String> requestType;

    private List<ResponseMessage> responseForInvalidRequest;

    @NotNull( message = "Client Id should not null.")
    private Long clientId;

    @NotNull( message = "Application Id should not null.")
    private Long applicationId;

    private String responseTemplate;

    @Transient
    private ArrayList<ServiceParameter> serviceParameters;


}

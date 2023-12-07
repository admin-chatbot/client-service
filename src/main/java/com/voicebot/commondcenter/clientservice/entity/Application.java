package com.voicebot.commondcenter.clientservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.io.Serializable;



@Document(collection = "application")
@Data
@Builder
public class Application implements Serializable,BaseEntity {

    @Transient
    public static final String SEQUENCE_NAME = "application_sequence";

    @Id
    private Long id;

    @NotBlank( message = "Name should not null or empty.")
    private String name;

    @NotBlank( message = "Purpose should not null or empty.")
    private String purpose;

    @NotBlank( message = "Service Sources URL should nit null or empty.")
    private String sourceUrl;

    private AuthMechanisms authMechanisms;

    @NotBlank( message = "Service Doc URL should not null or empty.")
    private String serviceDocUrl;

    @NotNull(message = "Client Id should not null or empty.")
    private Long clintId;

}

package com.voicebot.commondcenter.clientservice.entity;

import com.voicebot.commondcenter.clientservice.enums.Status;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


@Document(collection = "application")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Application extends AbstractBaseEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "application_sequence";

    @Id
    private Long id;

    @Indexed(unique = true)
    @NotBlank( message = "Name should not null or empty.")
    private String name;

    @NotBlank( message = "Purpose should not null or empty.")
    private String purpose;

    @NotBlank( message = "Service Sources URL should nit null or empty.")
    private String sourceUrl;

    private AuthMechanisms authMechanisms;

    @NotBlank( message = "Service Doc URL should not null or empty.")
    private String serviceDocUrl;

    private Date registerDate;

    @NotBlank( message = "Status should nit null or empty.")
    private Status status;

    @NotNull(message = "Client Id should not null or empty.")
    private Long clintId;





}

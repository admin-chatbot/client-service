package com.voicebot.commondcenter.clientservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;



@Document(collection = "application")
@Data
@Builder
public class Application implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "application_sequence";

    @Id
    private Long id;

    private String name;

    private String purpose;

    private String sourceUrl;

    private AuthMechanisms authMechanisms;

    private String serviceDocUrl;

    private Long clintId;

}

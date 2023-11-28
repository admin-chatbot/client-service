package com.voicebot.commondcenter.clientservice.entity;


import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "serviceparameters")
@Data
@Builder
public class ServiceParameter {

    @Transient
    public static final String SEQUENCE_NAME = "serviceparameter_sequence";

    @Id
    private Long id;

    private Long serviceId;

    private String name;

    private String description;

    private String type; //NUMBER/BOOLEAN/STRING/DATE/TIMESTAMP

    private String value;

    private String questionToGetInput;

    private Boolean isRequired;

    @Transient
    private Boolean questionAsked;



}

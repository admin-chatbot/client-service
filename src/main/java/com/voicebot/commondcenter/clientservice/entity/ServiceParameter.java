package com.voicebot.commondcenter.clientservice.entity;


import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

    private Boolean required;

    private String type;

    private String paramType;

    private String in;

    private String value;

    private List<String> questionToGetInput;

    @Transient
    private Boolean questionAsked;



}

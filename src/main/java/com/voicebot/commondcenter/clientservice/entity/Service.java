package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.utils.ObjectIdJsonSerializer;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "service")
@Data
@Builder
public class Service {

    @Transient
    public static final String SEQUENCE_NAME = "service_sequence";

    @Id
    private Long id;

    private String serviceName;

    private ArrayList<String> keyword;

    private List<String> serviceResponseType;

    private List<ResponseMessage> responseForInvalidRequest;

    private String requestType;

    private String serviceEndpoint;

    private Long clientId;

    @Transient
    private ArrayList<ServiceParameter> serviceParameters;


}

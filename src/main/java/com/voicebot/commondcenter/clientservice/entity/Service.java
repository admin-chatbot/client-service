package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.discovery.service.model.Authorization;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ResponseMessage;
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

    private String endpoint;

    private String method; //GET/POST/PUT

    private String name;

    private String summary;

    private Authorization authorization;

    private List<String> responseType;

    private List<String> requestType;

    private List<ResponseMessage> responseForInvalidRequest;

    private List<String> questionToBeAsked;

    private Long clientId;

    private String responseTemplate;

    @Transient
    private ArrayList<ServiceParameter> serviceParameters;


}

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

@Document(collection = "service")
@Data
@Builder
public class Service {

    @Id
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    private String serviceName;

    private ArrayList<String> keyword;

    private String serviceResponseType;

    private String requestType;

    private String serviceEndpoint;

    private ObjectId clientId;

    @Transient
    private ArrayList<ServiceParameter> serviceParameters;


}

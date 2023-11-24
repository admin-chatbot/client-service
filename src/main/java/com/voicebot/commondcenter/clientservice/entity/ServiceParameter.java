package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.utils.ObjectIdJsonSerializer;
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

    @Id
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    private ObjectId serviceId;

    private String name;

    private String type; //NUMBER/BOOLEAN/STRING/DATE/TIMESTAMP

    private String value;

    private String questionToGetInput;

    private Boolean isRequired;

    @Transient
    private Boolean questionAsked;

}

package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.utils.ObjectIdJsonSerializer;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "client")
@Data
@Builder
public class Client {

    @Id
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    private String clientName;

    private Date registerDate;

    private String category;

}

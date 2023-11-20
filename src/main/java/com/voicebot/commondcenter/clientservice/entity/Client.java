package com.voicebot.commondcenter.clientservice.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.voicebot.commondcenter.clientservice.utils.ObjectIdJsonSerializer;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Date;

@Document(collection = "client")
@Data
@Builder
public class Client {
    @Id
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    private ObjectId id;

    @NotBlank(message = "Client Name should not null or empty.")
    private String clientName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", locale = "en-PH")
    private Date registerDate;

    @Email(message = "Please provide valid email.")
    private String email;

    @NotBlank(message = "Password is mandatory.")
    private String password;

    private java.util.Date lastLogin;

    private String token;

    private java.util.Date expire;

}

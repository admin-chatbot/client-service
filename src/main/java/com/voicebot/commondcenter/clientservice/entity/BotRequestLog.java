package com.voicebot.commondcenter.clientservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "bot_request_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotRequestLog extends AbstractBaseEntity implements  Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "bot_request_sequence";

    @Id
    private Long id;

    private String requestId;

    private String userId;

    @Field(name = "user_input")
    private String question;

    private String response;

    private Date requestDate;

    @Field(name = "user_intent")
    private String intent;

    @Field(name = "client_id")
    private Long client;

    private Long serviceLogId;

}
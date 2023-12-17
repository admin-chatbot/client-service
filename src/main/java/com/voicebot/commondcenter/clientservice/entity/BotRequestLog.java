package com.voicebot.commondcenter.clientservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "botrequestlog")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotRequestLog implements BaseEntity, Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "bot_request_sequence";

    @Id
    private Long id;

    private String userName;

    private String question;

    private String response;

    private Date requestDate;

    private Boolean isSuccess;

    private Long client;

    private Long application;

    private String serviceEndpoint;

}

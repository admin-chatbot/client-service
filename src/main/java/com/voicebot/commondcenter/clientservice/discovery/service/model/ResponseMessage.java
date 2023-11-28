package com.voicebot.commondcenter.clientservice.discovery.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage {

    private String code;
    public String message;

}

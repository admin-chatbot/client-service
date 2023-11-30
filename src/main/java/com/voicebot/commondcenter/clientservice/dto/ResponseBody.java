package com.voicebot.commondcenter.clientservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBody {

    private int code;

    private String message;

    private Exception exception;
}

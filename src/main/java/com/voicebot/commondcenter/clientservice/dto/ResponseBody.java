package com.voicebot.commondcenter.clientservice.dto;


import com.voicebot.commondcenter.clientservice.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@Builder
public class ResponseBody<I extends Object>  {

    private int code;
    private String message;
    private I data;
    private Exception exception;

    public static ResponseBody badRequest(String message) {
        return ResponseBody.builder().code(HttpStatus.BAD_REQUEST.value()).message(message).build();
    }

    public static ResponseBody internalServerError(Exception exception) {
        return ResponseBody.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(exception.getMessage()).exception(exception).build();
    }
}

package com.voicebot.commondcenter.clientservice.dto;


import com.voicebot.commondcenter.clientservice.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ResponseBody<I extends Serializable>  {

    private int code;

    private String message;

    private I data;

    private Exception exception;
}

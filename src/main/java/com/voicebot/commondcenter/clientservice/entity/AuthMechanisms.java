package com.voicebot.commondcenter.clientservice.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AuthMechanisms {

    private String Type;
    private Map<String, String> parameters;

}

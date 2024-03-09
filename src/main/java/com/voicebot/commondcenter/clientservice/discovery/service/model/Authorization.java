package com.voicebot.commondcenter.clientservice.discovery.service.model;

import com.voicebot.commondcenter.clientservice.enums.AuthorizationType;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Authorization {
    private AuthorizationType authorizationType;
    private Map<String,String> params;
}

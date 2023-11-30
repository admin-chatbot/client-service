package com.voicebot.commondcenter.clientservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutoDiscoverServiceRequestBody {
    private String endpoint;

    private String method;

    private String name;
}

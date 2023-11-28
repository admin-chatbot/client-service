package com.voicebot.commondcenter.clientservice.discovery.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    private String endpoint;

    private String method; //GET/POST/PUT

    private String name;

    private String summary;

    private Authorization authorization;

    private List<String> responseType;

    private List<String> requestType;

    private List<ResponseMessage> responseForInvalidRequest;

    private List<ServiceParameter> parameters;

}

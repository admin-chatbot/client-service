package com.voicebot.commondcenter.clientservice.discovery.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceParameterDTO {

    private String name;

    private String description;

    private Boolean required;

    private String type;

    private String paramType;

    private String in;



}

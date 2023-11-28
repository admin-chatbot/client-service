package com.voicebot.commondcenter.clientservice.discovery.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceParameter {

    private String name;

    private String discription;

    private Boolean required;

    private String type;

    private String paramType;

    private String in;



}

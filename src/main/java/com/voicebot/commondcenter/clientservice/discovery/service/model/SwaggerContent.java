package com.voicebot.commondcenter.clientservice.discovery.service.model;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwaggerContent {
    private String basePath;
    private String info;
    private Authorization authorization;
    private List<String> produces;
    private List<Service> services;
    private String apiVersion;

}

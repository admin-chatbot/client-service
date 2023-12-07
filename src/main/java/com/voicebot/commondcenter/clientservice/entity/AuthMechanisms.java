package com.voicebot.commondcenter.clientservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthMechanisms implements BaseEntity {

    private String Type;
    private Map<String, String> parameters;

}

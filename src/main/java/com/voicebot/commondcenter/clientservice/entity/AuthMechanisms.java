package com.voicebot.commondcenter.clientservice.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthMechanisms  implements Serializable {

    private String Type;
    private Map<String, String> parameters;

}

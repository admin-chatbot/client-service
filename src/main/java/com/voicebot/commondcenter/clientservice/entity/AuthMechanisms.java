package com.voicebot.commondcenter.clientservice.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthMechanisms extends AbstractBaseEntity implements Serializable {

    private String Type;
    private Map<String, String> parameters;

}

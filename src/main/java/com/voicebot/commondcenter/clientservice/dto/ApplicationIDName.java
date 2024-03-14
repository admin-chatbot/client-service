package com.voicebot.commondcenter.clientservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationIDName {
    private Long id;
    private String Name;
}

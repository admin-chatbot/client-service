package com.voicebot.commondcenter.clientservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogsStatus {
        private int success;
        private int fail;
}

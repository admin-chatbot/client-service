package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.enums.ServiceLogStatus;
import com.voicebot.commondcenter.clientservice.enums.TimeFrame;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DashboardSearchRequest {
   private Long clientId;
   private ServiceLogStatus status;
   private TimeFrame timeFrame;
   private String serviceUserOption;
   private Long application;
}

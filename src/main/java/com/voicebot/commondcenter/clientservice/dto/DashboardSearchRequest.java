package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.Application;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DashboardSearchRequest {

   private Long clientId;
   private String status;
   private String timeFrame;
   private String serviceUserOption;

}

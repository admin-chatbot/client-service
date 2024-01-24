package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.Application;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class ServiceSearchRequest {


   private String name;
   private String endPoint;
   private Long method;
   private String status;
   private Long clientId;

   public Application build() {
      return null;
   }
}

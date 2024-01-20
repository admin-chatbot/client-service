package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.Application;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class ApplicationSearchRequest {

   private String purpose;
   private String name;
   private Date toDate;
   private Date  fromDate;
   private String status;


   public Application build() {
      return null;
   }
}

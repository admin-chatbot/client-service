package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class UserSearchRequest {

   private String empId;
   private String name;
   private int mobile;
   private String  access;
   private String status;


   public User build() {
      return null;
   }
}

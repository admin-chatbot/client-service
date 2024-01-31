package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import com.voicebot.commondcenter.clientservice.service.ServiceLogService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;


import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private List<ServiceLog> serviceLogs;
    private Map<String, Integer> serviceCallsByApplication;
    private Map<String, Integer> serviceCallsByServiceOrUser;
}

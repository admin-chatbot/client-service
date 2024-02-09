package com.voicebot.commondcenter.clientservice.dto;

import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto_backup {
    private List<ServiceLog> serviceLogs;
    private Map<String, Integer> serviceCallsByStatus;
    private Map<Long, Integer> serviceCallsByApplication;
    private Map<String, Integer> serviceCallsByServiceOrUser;
}

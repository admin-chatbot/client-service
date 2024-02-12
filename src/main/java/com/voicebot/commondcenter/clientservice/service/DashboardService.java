package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.dashboard.ServiceLogs;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface DashboardService {
    //public ApplicationService getApplicationService();

 
    DashboardDto getDashboard();

    DashboardDto getDashboardByClintId(Long clientId);

    ServiceLogs getServiceLogs(DashboardSearchRequest dashboardSearchRequest);

    Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> getDashboardByClientIdAndStatusAndTimeframe(DashboardSearchRequest dashboardSearchRequest);


}

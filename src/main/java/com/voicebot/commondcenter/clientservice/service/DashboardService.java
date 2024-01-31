package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DashboardService {
    //public ApplicationService getApplicationService();



    public List<ServiceLog> getDashboardByClientIdAndStatusAndTimeframe(DashboardSearchRequest dashboardSearchRequest);

}

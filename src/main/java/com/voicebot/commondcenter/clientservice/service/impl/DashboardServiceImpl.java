package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ServiceLogService serviceLogService;

    @Autowired
    public ServiceLogService getServiceLogService() {
        return serviceLogService;
    }

    @Override
    public List<ServiceLog> getDashboardByClientIdAndStatusAndTimeframe(DashboardSearchRequest dashboardSearchRequest) {
     /*   DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setServiceLogs(null);
        dashboardDto.setServiceCallsByApplication(null);
        dashboardDto.setServiceCallsByServiceOrUser(null);*/
        List<ServiceLog> serviceLogs = serviceLogService.getServiceLogCountByStatusAndDate(dashboardSearchRequest);
        return serviceLogs;
    }
}

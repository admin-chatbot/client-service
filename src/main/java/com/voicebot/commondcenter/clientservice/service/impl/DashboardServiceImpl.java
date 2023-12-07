package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import com.voicebot.commondcenter.clientservice.repository.ApplicationRepository;
import com.voicebot.commondcenter.clientservice.repository.ServiceRepository;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.DashboardService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    public ApplicationService getApplicationService() {
            return applicationService;
    }

    @Autowired
    public ServiceService getServiceService() {
        return serviceService;
    }

    @Override
    public DashboardDto getDashboard() {
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setApplicationCount(applicationService.find().size());
        dashboardDto.setServiceCount(serviceService.find().size());

        return dashboardDto;
    }


}

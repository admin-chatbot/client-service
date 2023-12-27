package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceLogService serviceLogService;

    @Autowired    public BotRequestLogService botRequestLogService;
    @Autowired
    public ApplicationService getApplicationService() {
        return applicationService;
    }

    @Autowired
    public BotRequestLogService getBotRequestLogService() {
        return botRequestLogService;
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
        dashboardDto.setTopUsed10Services(serviceLogService.getMaximumCountByServiceName());
        dashboardDto.setMostActiveClient(botRequestLogService.getMostActiveClient());
        dashboardDto.setLeastActiveClient(botRequestLogService.getLeastActiveClient());
        //dashboardDto.setCountOfSucccessCalls(botRequestLogService.countOfSuccessCalls());

        return dashboardDto;
    }

    @Override
    public DashboardDto getDashboardByClintId(Long clintId) {
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setApplicationCount(applicationService.findByClint(clintId).size());
        dashboardDto.setServiceCount(serviceService.findAllByClientId(clintId).size());
        dashboardDto.setTopUsed10Services(serviceLogService.getMaximumCountByServiceName());
        dashboardDto.setMostActiveClient(botRequestLogService.getMostActiveClient());
        dashboardDto.setLeastActiveClient(botRequestLogService.getLeastActiveClient());
        //dashboardDto.setCountOfSucccessCalls(botRequestLogService.countOfSuccessCalls());

        return dashboardDto;
    }
}

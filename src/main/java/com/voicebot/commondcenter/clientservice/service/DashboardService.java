package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    //public ApplicationService getApplicationService();

    DashboardDto getDashboard();

    DashboardDto getDashboardByClintId(Long clientId);

}

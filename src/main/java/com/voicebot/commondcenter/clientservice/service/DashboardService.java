package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DashboardService {
    public ApplicationService getApplicationService();

    public DashboardDto getDashboard();

}

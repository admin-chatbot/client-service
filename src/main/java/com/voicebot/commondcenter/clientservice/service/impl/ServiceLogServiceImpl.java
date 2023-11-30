package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.repository.ServiceLogRepository;
import com.voicebot.commondcenter.clientservice.service.ServiceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceLogServiceImpl implements ServiceLogService {

    @Autowired
    private ServiceLogRepository serviceLogRepository;

    @Override
    public ServiceLog save(ServiceLog serviceLog) {
        return serviceLogRepository.save(serviceLog);
    }

    @Override
    public List<ServiceLog> get() {
        return serviceLogRepository.findAll();
    }

    @Override
    public Page<ServiceLog> get(Pageable pageable) {
        return serviceLogRepository.findAll(pageable);
    }

    @Override
    public Long getTotalCount() {
        return null;
    }

    @Override
    public Long getTotalCount(String serviceName) {
        return null;
    }

    @Override
    public Long getTotalByClient(Long id) {
        return null;
    }

    @Override
    public Long getTotalByClient(String clientName) {
        return null;
    }

    @Override
    public Long getTotalByApplication(Long id) {
        return null;
    }

    @Override
    public Long getTotalByApplication(String clientName) {
        return null;
    }

    @Override
    public Long getTotalByClientAndApplication(Long id) {
        return null;
    }

    @Override
    public Long getTotalByClientAndApplication(String clientName) {
        return null;
    }

    @Override
    public List<String> getTopNServices(int n) {
        return null;
    }
}

package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ServiceCountDto;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceLogService {

    public ServiceLog save(ServiceLog serviceLog);

    public List<ServiceLog> get();

    public Page<ServiceLog> get(Pageable pageable);

    public Long getTotalCount();

    public Long getTotalCount(String serviceName);

    public Long getTotalByClient(Long id);

    public Long getTotalByClient(String clientName);

    public Long getTotalByApplication(Long id);

    public List<ServiceCountDto> getMaximumCountByServiceName();

    public List<ServiceCountDto> getMinimumCountByServiceName();

    public Long getTotalByApplication(String clientName);

    public Long getTotalByClientAndApplication(Long id);

    public Long getTotalByClientAndApplication(String clientName);

    public List<String> getTopNServices(int n);


    public List<ServiceCountDto> getMaximumCountByServiceNameByClient(Long clintId);

    public List<ServiceLog> getServiceLogCountByStatusAndDate(DashboardSearchRequest dashboardSearchRequest);

}

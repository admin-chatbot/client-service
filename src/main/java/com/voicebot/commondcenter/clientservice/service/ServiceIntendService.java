package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.ServiceIntends;
import com.voicebot.commondcenter.clientservice.exception.ServiceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ServiceIntendService extends BaseService<ServiceIntends>{
    ServiceIntends save(ServiceIntends serviceIntend) throws ServiceNotFoundException;
    List<ServiceIntends> findAll();
    List<ServiceIntends> findByName(String name);

    List<ServiceIntends> findByServiceId(Long serviceId);

    List<ServiceIntends> findByApplicationId(Long applicationId);

    ServiceIntends findById(Long id);

    ServiceIntends update(ServiceIntends serviceIntend);

    void delete(Long id);

    void delete(ServiceIntends serviceIntend);

    void deleteAll();

    boolean existsById(Long id);


    boolean existsByName(String name);

    boolean existsByApplicationId(Long applicationId);

    boolean existsByApplicationIdAndName(Long applicationId, String name);



}

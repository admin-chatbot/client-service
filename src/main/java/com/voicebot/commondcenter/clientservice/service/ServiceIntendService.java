package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.ServiceIntend;
import com.voicebot.commondcenter.clientservice.exception.ServiceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ServiceIntendService extends BaseService<ServiceIntend>{
    ServiceIntend save(ServiceIntend serviceIntend) throws ServiceNotFoundException;
    List<ServiceIntend> findAll();
    List<ServiceIntend> findByName(String name);

    List<ServiceIntend> findByServiceId(Long serviceId);

    Optional<ServiceIntend> findByIntend(String intend);

    List<ServiceIntend> findByApplicationId(Long applicationId);

    ServiceIntend findById(Long id) throws ServiceNotFoundException;

    ServiceIntend update(ServiceIntend serviceIntend);

    void delete(Long id);

    void delete(ServiceIntend serviceIntend);

    void deleteAll();

    boolean existsById(Long id);


    boolean existsByIntend(String name);





}

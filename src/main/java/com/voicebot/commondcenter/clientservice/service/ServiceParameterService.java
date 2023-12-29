package com.voicebot.commondcenter.clientservice.service;


import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.repository.ServiceParameterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ServiceParameterService {

    public ServiceParameterRepository getRepository();

    public ServiceParameter save(ServiceParameter serviceParameter);

    public ServiceParameter edit(ServiceParameter serviceParameter);

    public List<ServiceParameter> save(List<ServiceParameter> serviceParameters);

    public List<ServiceParameter> findByServiceId(Long serviceId);

    public Page<ServiceParameter> find(Pageable pageable);

    public List<ServiceParameter> find();

    public Optional<ServiceParameter> findOne(Long id);


}

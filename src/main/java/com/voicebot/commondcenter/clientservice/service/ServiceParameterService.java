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

     ServiceParameterRepository getRepository();
     ServiceParameter save(ServiceParameter serviceParameter);
     ServiceParameter edit(ServiceParameter serviceParameter);
     List<ServiceParameter> save(List<ServiceParameter> serviceParameters);
     List<ServiceParameter> findByServiceId(Long serviceId);
     Page<ServiceParameter> find(Pageable pageable);
     List<ServiceParameter> find();
     Optional<ServiceParameter> findOne(Long id);

}

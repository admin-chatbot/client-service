package com.voicebot.commondcenter.clientservice.service.impl;


import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.repository.ServiceRepository;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;


    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> find() {
        return serviceRepository.findAll();
    }

    @Override
    public com.voicebot.commondcenter.clientservice.entity.Service save(com.voicebot.commondcenter.clientservice.entity.Service service) {
        service.setId(sequenceGeneratorService.generateSequence(com.voicebot.commondcenter.clientservice.entity.Service.SEQUENCE_NAME));
        service.setStatus(Status.NEW);
        service.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        service.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        return serviceRepository.save(service);
    }

    @Override
    public com.voicebot.commondcenter.clientservice.entity.Service edit(com.voicebot.commondcenter.clientservice.entity.Service service) {
        if(service!=null)
            service.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert service != null;
        return serviceRepository.save(service);
    }

    @Override
    public Optional<com.voicebot.commondcenter.clientservice.entity.Service> fetchOne(Long serviceId) {
        return serviceRepository.findById(serviceId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findAllByClientId(Long clientId) {
        return serviceRepository.findServicesByClientId(clientId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findAllByApplicationId(Long applicationId) {
        return serviceRepository.findServiceByApplicationId(applicationId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findServiceByClientIdAndKeywordLike(Long clientId, String keyword) {
        return serviceRepository.findServicesByClientIdAndKeywordLike(clientId, keyword);
    }
}

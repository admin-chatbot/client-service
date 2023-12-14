package com.voicebot.commondcenter.clientservice.service.impl;


import com.voicebot.commondcenter.clientservice.repository.ServiceRepository;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return serviceRepository.save(service);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findAllByClientId(Long clientId) {
        return serviceRepository.findServicesByClientId(clientId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findServiceByClientIdAndKeywordLike(Long clientId, String keyword) {
        return serviceRepository.findServicesByClientIdAndKeywordLike(clientId, keyword);
    }
}

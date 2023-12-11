package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.repository.ServiceParameterRepository;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.ServiceParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceParameterServiceImpl implements ServiceParameterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceParameterService.class);

    @Autowired
    private ServiceParameterRepository repository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Override
    public ServiceParameterRepository getRepository() {
        return repository;
    }

    @Override
    public ServiceParameter save(ServiceParameter serviceParameter) {
        serviceParameter.setId(sequenceGeneratorService.generateSequence(ServiceParameter.SEQUENCE_NAME));
        return repository.save(serviceParameter);
    }

    @Override
    public List<ServiceParameter> findByServiceId(Long serviceId) {
        Example<ServiceParameter> serviceParameterExample
                = Example.of(ServiceParameter.builder().serviceId(serviceId).build());
        return repository.findAll(serviceParameterExample);

    }

    @Override
    public Page<ServiceParameter> find(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<ServiceParameter> find() {
        return repository.findAll();
    }

    @Override
    public Optional<ServiceParameter> findOne(Long id) {
        return repository.findById(id);
    }
}

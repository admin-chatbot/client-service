package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.repository.ApplicationRepository;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;


    @Override
    public ApplicationRepository repository() {
        return applicationRepository;
    }

    @Override
    public Application onBoard(Application application) {
        application.setId(sequenceGeneratorService.generateSequence(Application.SEQUENCE_NAME));
        application.setRegisterDate(new Date(System.currentTimeMillis()));
        application.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        application.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        application.setStatus(Status.NEW);
        return applicationRepository.save(application);
    }

    @Override
    public Application edit(Application application) {
        if(application!=null)
            application.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert application != null;
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> find() {
        return applicationRepository.findAll();
    }

    @Override
    public Page<Application> find(Pageable pageable) {
        return applicationRepository.findAll(pageable);
    }

    @Override
    public Optional<Application> findOne(Long id) {
        return applicationRepository.findById(id);
    }



    @Override
    public List<Application> findByClint(Long id) {
        return applicationRepository.findApplicationsByClintId(id);
    }

    @Override
    public Page<Application> findByClient(Pageable pageable, Long id) {
        return applicationRepository.findApplicationsByClintId(id,pageable);
    }
}

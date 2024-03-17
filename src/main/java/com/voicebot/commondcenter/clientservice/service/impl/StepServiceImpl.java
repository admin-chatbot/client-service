package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Step;
import com.voicebot.commondcenter.clientservice.repository.StepRepository;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.StepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StepServiceImpl implements StepService, BaseService<Step> {

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public StepRepository getRepository() {
        return stepRepository;
    }


    @Override
    public Step onBoard(Step step) {
        step.setId(sequenceGeneratorService.generateSequence(Application.SEQUENCE_NAME));
        return stepRepository.save(step);
    }

    @Override
    public Step edit(Step step) {
        if(step!=null)
            step.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert step != null;
        return stepRepository.save(step);
    }

    @Override
    public List<Step> find() {
        return stepRepository.findAll();
    }

    public Optional<Step> findById(Long id) {
        return stepRepository.findById(id);
    }

    @Override
    public Optional<Step> findAStepByClientIdAndIntent(Long id,String intent) {
        Example<Step> stepExample = Example.of(Step.builder().clientId(id).intent(intent).build());
        return findOneByExample(stepExample);
    }

    @Override
    public List<Step> findStepByClientId(Long clientId) {
        return stepRepository.findStepsByClientId(clientId);
    }

    @Override
    public Optional<Step> findOneByExample(Example<Step> stepExample) {
        return Optional.empty();
    }

    @Override
    public List<Step> findByExample(Example<Step> stepExample) {
        return null;
    }

    @Override
    public Page<Step> findByExample(Example<Step> stepExample, Pageable pageable) {
        return null;
    }

    @Override
    public List<Step> search(Step step) {
        return null;
    }

    @Override
    public Page<Step> search(Step step, Pageable pageable) {
        return null;
    }
}

package com.voicebot.commondcenter.clientservice.service;


import com.voicebot.commondcenter.clientservice.entity.Step;
import com.voicebot.commondcenter.clientservice.repository.StepRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StepService {

    StepRepository getRepository();
    Step onBoard(Step step);
    Step edit(Step step);
    List<Step> find();
    public Optional<Step> findById(Long id);
    public Optional<Step> findAStepByClientIdAndIntent(Long id,String intent);
    List<Step> findStepByClientId(Long clientId);
    Optional<Step> findOneByExample(Example<Step> stepExample);


}

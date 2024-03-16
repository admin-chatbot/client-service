package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Step;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepRepository extends MongoRepository<Step,Long> {
    public Optional<Step> findById(Long id);
    public List<Step> findStepsByClientId(Long clientId);

}

package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;

import com.voicebot.commondcenter.clientservice.entity.Story;
import com.voicebot.commondcenter.clientservice.repository.StepRepository;
import com.voicebot.commondcenter.clientservice.repository.StoryRepository;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.StepService;
import com.voicebot.commondcenter.clientservice.service.StoryService;
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
public class StoryServiceImpl implements StoryService, BaseService<Story> {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public StoryRepository getRepository() {
        return storyRepository;
    }


    @Override
    public Story onBoard(Story story) {
        story.setId(sequenceGeneratorService.generateSequence(Application.SEQUENCE_NAME));
        return storyRepository.save(story);
    }

    @Override
    public Story edit(Story story) {
        if(story!=null)
            story.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert story != null;
        return storyRepository.save(story);
    }

    @Override
    public List<Story> find() {
        return storyRepository.findAll();
    }

    public Optional<Story> findById(Long id) {
        return storyRepository.findById(id);
    }



    @Override
    public List<Story> findStoriesByClientId(Long clientId) {
        return storyRepository.findStoriesByClientId(clientId);
    }

    @Override
    public Optional<Story> findOneByExample(Example<Story> storyExample) {
        return Optional.empty();
    }

    @Override
    public List<Story> findByExample(Example<Story> stepExample) {
        return null;
    }

    @Override
    public Page<Story> findByExample(Example<Story> storyExample, Pageable pageable) {
        return null;
    }

    @Override
    public List<Story> search(Story story) {
        return null;
    }

    @Override
    public Page<Story> search(Story step, Pageable pageable) {
        return null;
    }
}

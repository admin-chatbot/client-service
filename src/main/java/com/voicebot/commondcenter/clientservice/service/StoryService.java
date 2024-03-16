package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Story;
import com.voicebot.commondcenter.clientservice.repository.StoryRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StoryService {

    StoryRepository getRepository();
    Story onBoard(Story story);
    Story edit(Story story);
    List<Story> find();
    public Optional<Story> findById(Long id);
    List<Story> findStoriesByClientId(Long clientId);
    Optional<Story> findOneByExample(Example<Story> storyExample);
}

package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Step;
import com.voicebot.commondcenter.clientservice.entity.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends MongoRepository<Story,Long> {
    public Optional<Story> findById(Long id);
    public List<Story> findStoriesByClientId(Long clientId);
    public List<Story> findStoriesByStepId(Long stepId);

}

package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Rule;
import com.voicebot.commondcenter.clientservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleRepository extends MongoRepository<Rule,Long> {
    public Optional<Rule> findById(Long id);
    public List<Rule> findRulesByClientId(Long clientId);

}

package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Rule;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.repository.RuleRepository;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.RuleService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
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
public class RuleServiceImpl implements RuleService, BaseService<Rule> {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public RuleRepository getRepository() {
        return ruleRepository;
    }


    @Override
    public Rule onBoard(Rule rule) {
        rule.setId(sequenceGeneratorService.generateSequence(Application.SEQUENCE_NAME));
        return ruleRepository.save(rule);
    }

    @Override
    public Rule edit(Rule rule) {
        if(rule!=null)
            rule.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert rule != null;
        return ruleRepository.save(rule);
    }

    @Override
    public List<Rule> find() {
        return ruleRepository.findAll();
    }


    public Optional<Rule> findById(Long id) {
        return ruleRepository.findById(id);
    }

    @Override
    public List<Rule> findByExample(Example<Rule> ruleExample) {
        return null;
    }

    @Override
    public Page<Rule> findByExample(Example<Rule> ruleExample, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Rule> findOneByExample(Example<Rule> ruleExample) {
        return Optional.empty();
    }

    @Override
    public List<Rule> search(Rule rule) {
        return null;
    }

    @Override
    public Page<Rule> search(Rule rule, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Rule> findARuleByClientIdAndRuleName(Long id,String name) {
        Example<Rule> ruleExample = Example.of(Rule.builder().clientId(id).rulename(name).build());
        return findOneByExample(ruleExample);
    }

    @Override
    public List<Rule> findRulesByClientId(Long clientId) {
        return ruleRepository.findRulesByClientId(clientId);
    }

}

package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Rule;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.repository.RuleRepository;
import com.voicebot.commondcenter.clientservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface RuleService {

    RuleRepository getRepository();
    Rule onBoard(Rule rule);
    Rule edit(Rule rule);
    List<Rule> find();
    public Optional<Rule> findById(Long id);
    public Optional<Rule> findARuleByClientIdAndRuleName(Long id,String name);
    List<Rule> findRulesByClientId(Long clientId);


}

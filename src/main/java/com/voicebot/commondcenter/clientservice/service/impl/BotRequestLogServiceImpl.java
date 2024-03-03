package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.repository.BotRequestLogRepository;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.BotRequestLogService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.voicebot.commondcenter.clientservice.service.BaseService;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BotRequestLogServiceImpl implements BotRequestLogService, BaseService<BotRequestLog> {

    Logger logger = Logger.getLogger(ServiceLogServiceImpl.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BotRequestLogRepository botRequestLogRepository;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;


    @Override
    public BotRequestLogRepository getRepository() {
        return botRequestLogRepository;
    }

    @Override
    public List<BotRequestLog> findByExample(Example<BotRequestLog> botRequestLogExample) {
        System.out.println("Input passed to botRquestLog: " +botRequestLogExample);
        System.out.println("Output from botRquestLog: " +botRequestLogRepository.findAll(botRequestLogExample).size());
        return botRequestLogRepository.findAll(botRequestLogExample);
    }

    public List<BotRequestLog> findLatestDocumentsForUser(String userId) {
            GroupOperation groupByRequest = group("requestId")
                .first("$$ROOT").as("latestDocument");

        Aggregation aggregation = newAggregation(
                match(where("user_id").is(userId)),
                sort(Sort.Direction.ASC, "requestDate"),
                groupByRequest,
                replaceRoot("latestDocument")
        );

        return mongoTemplate.aggregate(aggregation, "botrequestlog", BotRequestLog.class).getMappedResults();
    }


    @Override
    public Optional<BotRequestLog> findBotRequestLogByUserAndRequest(String userId, String requestId) {
        System.out.println("From landing page in BotRequestLog");
        Example<BotRequestLog> botRequestLogExample = Example.of(BotRequestLog.builder().userId(userId).requestId(requestId).build());
        return findOneByExample(botRequestLogExample);
    }

    @Override
    public List<BotRequestLog> findBotRequestLogsByRequestId(String requestId) {
        return botRequestLogRepository.findBotRequestLogByRequestId(requestId);
    }

    @Override
    public BotRequestLog save(BotRequestLog botRequestLog) {
        botRequestLog.setId(sequenceGeneratorService.generateSequence(BotRequestLog.SEQUENCE_NAME));
        return botRequestLogRepository.save(botRequestLog);
    }

    @Override
    public Page<BotRequestLog> findByExample(Example<BotRequestLog> botRequestLogExample, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<BotRequestLog> findOneByExample(Example<BotRequestLog> botRequestLogExample) {

        return Optional.empty();
    }

    @Override
    public List<BotRequestLog> search(BotRequestLog botRequestLog) {
        return null;
    }

    @Override
    public Page<BotRequestLog> search(BotRequestLog botRequestLog, Pageable pageable) {
        return null;
    }

/*    public List<BotRequestLog> findBotRequestLogByName(String name) {
        System.out.println("username: " +name);
        System.out.println("Documents with username: " +botRequestLogRepository.findByUserName(name));
        return botRequestLogRepository.findByUserName(name);
    }*/
}

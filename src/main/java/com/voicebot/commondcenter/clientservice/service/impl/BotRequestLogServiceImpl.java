package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.ClientCountDto;
import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import com.voicebot.commondcenter.clientservice.repository.BotRequestLogRepository;
import com.voicebot.commondcenter.clientservice.service.BotRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BotRequestLogServiceImpl implements BotRequestLogService {

    Logger logger = Logger.getLogger(ServiceLogServiceImpl.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BotRequestLogRepository botRequestLogRepository;
    @Override
    public List<ClientCountDto> getMostActiveClient() {
        GroupOperation groupByClientId = Aggregation.group("client").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"));
        ProjectionOperation project = Aggregation.project("client", "count");
        Aggregation aggregation = Aggregation.newAggregation(groupByClientId, sortOperation, project);
        AggregationResults<ClientCountDto> output = mongoTemplate.aggregate(aggregation, "botrequestlog", ClientCountDto.class);

        logger.info("Raw results: {}" + output.getRawResults());
        logger.info("Mapped results: {}" + output.getMappedResults());
        return output.getMappedResults().stream().limit(10).toList();
    }

    @Override
    public List<ClientCountDto> getLeastActiveClient() {
        GroupOperation groupByClientId = Aggregation.group("client").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.ASC, "count"));
        ProjectionOperation project = Aggregation.project("client", "count");
        Aggregation aggregation = Aggregation.newAggregation(groupByClientId, sortOperation, project);
        AggregationResults<ClientCountDto> output = mongoTemplate.aggregate(aggregation, "botrequestlog", ClientCountDto.class);

        logger.info("Raw results: {}" + output.getRawResults());
        logger.info("Mapped results: {}" + output.getMappedResults());
        return output.getMappedResults().stream().limit(10).toList();
    }

    @Override
    public int countOfSuccessCalls() {
        Criteria criteria = Criteria.where("isSuccess").is(true);
        Query query = new Query(criteria);
        List<BotRequestLog> botRequestsSuccess = mongoTemplate.find(query, BotRequestLog.class);
        return botRequestsSuccess.size();
    }

    @Override
    public int countOfFailedCalls() {
        Criteria criteria = Criteria.where("isSuccess").is(false);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, BotRequestLog.class).size();
    }
}

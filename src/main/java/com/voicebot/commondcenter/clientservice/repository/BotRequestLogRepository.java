package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotRequestLogRepository extends MongoRepository<BotRequestLog, Long> {
    @Aggregation("{ $match: { userName: ?0 } }, { $sort: { requestDate: -1 } }, { $group: { _id: '$requestId', latestDocument: { $first: '$$ROOT' } } }")
    List<BotRequestLog> findLatestDocumentsForUser(String userName);

    List<BotRequestLog> findBotRequestLogByRequestId(String requestId);
}

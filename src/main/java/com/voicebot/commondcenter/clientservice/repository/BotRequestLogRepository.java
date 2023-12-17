package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import com.voicebot.commondcenter.clientservice.entity.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotRequestLogRepository extends MongoRepository<BotRequestLog,Long> {
    List<BotRequestLog> findBotRequestLogByIsSuccess(Boolean isSuccess);
}

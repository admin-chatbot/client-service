package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.UserSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.BotRequestLog;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.repository.BotRequestLogRepository;
import com.voicebot.commondcenter.clientservice.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.voicebot.commondcenter.clientservice.service.BaseService;

import java.util.List;
import java.util.Optional;

@Service
public interface BotRequestLogService {

     BotRequestLogRepository getRepository();
     List<BotRequestLog> findLatestDocumentsForUser(String userName);

     Optional<BotRequestLog> findBotRequestLogByUserAndRequest(String userName, String requestId);

     public BotRequestLog save(BotRequestLog botRequestLog);
}
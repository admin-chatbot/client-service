package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.ClientCountDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BotRequestLogService {

    List<ClientCountDto> getMostActiveClient();

    List<ClientCountDto> getLeastActiveClient();

    int countOfSuccessCalls();

    int countOfFailedCalls();

}

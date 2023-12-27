package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.ClientCountDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BotRequestLogService {

    public List<ClientCountDto> getMostActiveClient();

    public List<ClientCountDto> getLeastActiveClient();

    public int countOfSuccessCalls();

    public int countOfFailedCalls();

}

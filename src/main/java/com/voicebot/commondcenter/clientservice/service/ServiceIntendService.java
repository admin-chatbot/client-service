package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.ServiceIntends;
import com.voicebot.commondcenter.clientservice.exception.ServiceNotFoundException;
import org.springframework.stereotype.Service;


@Service
public interface ServiceIntendService extends BaseService<Authentication>{

    ServiceIntends save(ServiceIntends serviceIntend) throws ServiceNotFoundException;


}

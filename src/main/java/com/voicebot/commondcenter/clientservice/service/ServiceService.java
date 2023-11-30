package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Service;
import org.bson.types.ObjectId;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService {

    public Service save(Service service);

    List<Service> findAllByClientId(Long clientId);
}

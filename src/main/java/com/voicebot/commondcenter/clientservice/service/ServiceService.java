package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Service;
import org.bson.types.ObjectId;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService {

    public List<Service> find();
    public Service save(Service service);

    public Service edit(Service service);

    public Optional<Service> fetchOne(Long serviceId);

    List<Service> findAllByClientId(Long clientId);

    List<Service> findAllByApplicationId(Long applicationId);

    List<Service> findServiceByClientIdAndKeywordLike(Long clientId,String keyword);
}

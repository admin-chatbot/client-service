package com.voicebot.commondcenter.clientservice.service;


 

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ServiceSearchRequest;

 
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Service;
import org.bson.types.ObjectId;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService {
    List<Service> find();
    Service save(Service service);
    Service edit(Service service);
    Optional<Service> fetchOne(Long serviceId);
    List<Service> findAllByClientId(Long clientId);
    List<Service> findAllByApplicationId(Long applicationId);
    List<Service> findServiceByClientIdAndKeywordLike(Long clientId,String keyword);
    List<Service> search(ServiceSearchRequest serviceSearchRequest);
 
}

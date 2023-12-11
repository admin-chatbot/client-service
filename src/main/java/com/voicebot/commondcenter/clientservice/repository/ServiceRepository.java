package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Service;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends MongoRepository<Service, Long> {
    List<Service> findServicesByClientId(Long clientId);

    List<Service> findServicesByClientIdAndKeywordLike(Long clientId,String keyword);
}
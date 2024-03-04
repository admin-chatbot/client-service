package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.ServiceIntends;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceIntendRepository extends MongoRepository<ServiceIntends, Long> {
}

package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.ServiceIntend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceIntendRepository extends MongoRepository<ServiceIntend, Long> {
    List<ServiceIntend> findByServiceId(Long serviceId);
}

package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Service;
import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceParameterRepository extends MongoRepository<ServiceParameter, Long>{

}
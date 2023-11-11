package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, ObjectId> {
}

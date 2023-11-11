package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Client;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ClientService {

    public List<Client> findAll();

    public Optional<Client> findOne(ObjectId id);

    public Client save(Client client);


}

package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.exception.TokenNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ClientService {

    public List<Client> findAll();

    public Optional<Client> findOne(Long id);

    public Client save(Client client);

    public Client register(Client client) throws EmailAlreadyRegistered;

    public Client login(Login login) throws InvalidUserNameAndPassword;

    Optional<Client> authenticate(String accessToken) throws TokenNotFoundException;
}

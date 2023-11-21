package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.repository.ClientRepository;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.utils.EncryptDecryptPassword;
import com.voicebot.commondcenter.clientservice.utils.SecureTokenGenerator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;



    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findOne(ObjectId id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client register(Client client) throws EmailAlreadyRegistered {

        Optional<Client> alreadyPresent = clientRepository.findClientByEmail(client.getEmail());
        if (alreadyPresent.isPresent()) {
            throw new EmailAlreadyRegistered();
        }

        client.setPassword(EncryptDecryptPassword.encryptPassword(client.getPassword()));
        client.setRegisterDate(new Date(System.currentTimeMillis()));
        Client registeredClient = clientRepository.save(client);
        registeredClient.setPassword("");
        return client;
    }

    @Override
    public String login(Login login) throws InvalidUserNameAndPassword {

        Optional<Client> client = clientRepository.findClientByEmail(login.getEmail());
        if (client.isPresent()) {
            if(EncryptDecryptPassword.checkPassword(login.getPassword(),client.get().getPassword())){
               String token =  SecureTokenGenerator.nextToken();
               token = token + "-"+ System.currentTimeMillis();

                Client registeredClient = client.get();
                registeredClient.setToken(token);
                registeredClient.setLastLogin(new Timestamp(System.currentTimeMillis()));
                registeredClient.setExpire(new Timestamp(System.currentTimeMillis()));

                clientRepository.save(registeredClient);

                return token;

            } else {
                throw new InvalidUserNameAndPassword();
            }
        }
        throw new InvalidUserNameAndPassword();
    }

    @Override
    public Optional<Client> authenticate(String accessToken) {
        return Optional.empty();
    }
}

package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.exception.TokenNotFoundException;
import com.voicebot.commondcenter.clientservice.repository.ClientRepository;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.utils.EncryptDecryptPassword;
import com.voicebot.commondcenter.clientservice.utils.SecureTokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

   private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findOne(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findClientByEmail(email);
    }

    @Override
    public Client register(Client client) throws EmailAlreadyRegistered {

        Optional<Client> alreadyPresent = findClientByEmail(client.getEmail());
        if (alreadyPresent.isPresent()) {
            throw new EmailAlreadyRegistered();
        }

        client.setId(sequenceGeneratorService.generateSequence(Client.SEQUENCE_NAME));
        client.setRegisterDate(new Date(System.currentTimeMillis()));
        return clientRepository.save(client);
    }


    @Override
    public Optional<Client> findByAuthenticationId(Long id) {
        return clientRepository.findClientByAuthenticationId(id);
    }


}

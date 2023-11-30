package com.voicebot.commondcenter.clientservice.service.impl;


import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.repository.ClientRepository;
import com.voicebot.commondcenter.clientservice.repository.ServiceRepository;
import com.voicebot.commondcenter.clientservice.service.ClientService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
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
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public com.voicebot.commondcenter.clientservice.entity.Service save(com.voicebot.commondcenter.clientservice.entity.Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findAllByClientId(Long clientId) {
        return serviceRepository.findServiceByClientId(clientId);
    }
}

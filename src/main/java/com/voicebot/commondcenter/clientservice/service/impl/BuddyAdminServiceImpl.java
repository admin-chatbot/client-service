package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.repository.BuddyAdminRepository;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.BuddyAdminService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BuddyAdminServiceImpl implements BuddyAdminService, BaseService<BuddyAdmin> {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private BuddyAdminRepository buddyAdminRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;


    @Override
    public List<BuddyAdmin> findAll() {
        return buddyAdminRepository.findAll();
    }

    @Override
    public Optional<BuddyAdmin> findOne(Long id) {
        return buddyAdminRepository.findById(id);
    }

    @Override
    public BuddyAdmin save(BuddyAdmin buddyAdmin) {        ;
        return buddyAdminRepository.save(buddyAdmin);
    }

    @Override
    public BuddyAdmin register(Authentication authentication) throws Exception {

        Example<Authentication> findByEmailExample = Example.of(Authentication.builder().userName(authentication.getUserName()).build());
        Optional<Authentication> optionalAuthentication =  authenticationService.findOneByExample(findByEmailExample);
        if(optionalAuthentication.isPresent())
            throw new EmailAlreadyRegistered("Email is already link with another user");


        Example<BuddyAdmin> findBuddyAdminByEmailExample = Example.of(BuddyAdmin.builder().email(authentication.getUserName()).build());
        Optional<BuddyAdmin> optionalBuddyAdmin = findOneByExample(findBuddyAdminByEmailExample);
        if(optionalBuddyAdmin.isPresent())
            throw new EmailAlreadyRegistered("Email is already link with another user");

        try{

            Authentication registerUser = authenticationService.register(authentication);

            BuddyAdmin buddyAdmin = BuddyAdmin.builder()
                    .name(authentication.getName())
                    .contactNumber(authentication.getMobileNumber())
                    .email(authentication.getUserName())
                    .authenticationId(registerUser.getId())
                    .build();
            buddyAdmin.setId(sequenceGeneratorService.generateSequence(Client.SEQUENCE_NAME));
            buddyAdmin.setCreatedTimestamp(new Date(System.currentTimeMillis()));

            return save(buddyAdmin);

        }catch (Exception exception) {
            logger.error(exception.getMessage(),exception);
            throw new Exception("Unable to register.Please try after some time.");
        }


    }

    @Override
    public Optional<BuddyAdmin> findOneByAuthenticationId(Long id) {
        return buddyAdminRepository.findBuddyAdminByAuthenticationId(id);
    }

    @Override
    public List<BuddyAdmin> findByExample(Example<BuddyAdmin> buddyAdminExample) {
        return buddyAdminRepository.findAll(buddyAdminExample);
    }

    @Override
    public Page<BuddyAdmin> findByExample(Example<BuddyAdmin> buddyAdminExample, Pageable pageable) {
        return buddyAdminRepository.findAll(buddyAdminExample,pageable);
    }

    @Override
    public Optional<BuddyAdmin> findOneByExample(Example<BuddyAdmin> buddyAdminExample) {
        return buddyAdminRepository.findOne(buddyAdminExample);
    }

    @Override
    public List<BuddyAdmin> search(BuddyAdmin buddyAdmin) {
        return null;
    }

    @Override
    public Page<BuddyAdmin> search(BuddyAdmin buddyAdmin, Pageable pageable) {
        return null;
    }
}

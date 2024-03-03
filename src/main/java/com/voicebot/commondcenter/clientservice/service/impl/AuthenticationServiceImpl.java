package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.exception.TokenNotFoundException;
import com.voicebot.commondcenter.clientservice.repository.AuthenticationRepository;
import com.voicebot.commondcenter.clientservice.service.AuthenticationService;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.utils.EncryptDecryptPassword;
import com.voicebot.commondcenter.clientservice.utils.SecureTokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Optional<Authentication> login(Login login) throws InvalidUserNameAndPassword {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByUserName(login.getEmail());
        if (authentication.isPresent()) {
            if(EncryptDecryptPassword.checkPassword(login.getPassword(),authentication.get().getPassword())){
                String token =  SecureTokenGenerator.nextToken();
                token = token + "-"+ System.currentTimeMillis();

                Authentication registeredClient = authentication.get();
                registeredClient.setToken(token);
                registeredClient.setLastLogin(new Timestamp(System.currentTimeMillis()));
                registeredClient.setExpire(new Timestamp(System.currentTimeMillis()));

                try {
                    registeredClient = authenticationRepository.save(registeredClient);

                    return Optional.of(registeredClient) ;
                }catch (RuntimeException exception) {
                    throw new RuntimeException("Unable to login.Please try after some time");
                }

            } else {
                throw new InvalidUserNameAndPassword();
            }
        }
        throw new InvalidUserNameAndPassword();
    }

    @Override
    public Authentication save(Authentication authentication) {
        return authenticationRepository.save(authentication);
    }

    @Override
    public Authentication register(Authentication authentication) {
        authentication.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        authentication.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        authentication.setId(sequenceGeneratorService.generateSequence(Client.SEQUENCE_NAME));
        return authenticationRepository.save(authentication);
    }

    @Override
    public Authentication update(Authentication authentication) {
        return null;
    }

    @Override
    public Optional<Authentication> authenticate(String accessToken) throws TokenNotFoundException {
        if(StringUtils.isBlank(accessToken))
            throw new TokenNotFoundException();

        return authenticationRepository.findAuthenticationByToken(accessToken);
    }


    @Override
    public List<Authentication> findByExample(Example<Authentication> authenticationExample) {
        return authenticationRepository.findAll(authenticationExample);
    }

    @Override
    public Page<Authentication> findByExample(Example<Authentication> authenticationExample, Pageable pageable) {
        return authenticationRepository.findAll(authenticationExample,pageable);
    }

    @Override
    public Optional<Authentication> findOneByExample(Example<Authentication> authenticationExample) {
        return authenticationRepository.findOne(authenticationExample);
    }

    @Override
    public List<Authentication> search(Authentication authentication) {
        return null;
    }

    @Override
    public Page<Authentication> search(Authentication authentication, Pageable pageable) {
        return null;
    }
}

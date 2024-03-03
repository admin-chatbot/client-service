package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.*;
import com.voicebot.commondcenter.clientservice.enums.UserType;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.exception.TokenNotFoundException;
import com.voicebot.commondcenter.clientservice.repository.AuthenticationRepository;
import com.voicebot.commondcenter.clientservice.service.*;
import com.voicebot.commondcenter.clientservice.utils.EncryptDecryptPassword;
import com.voicebot.commondcenter.clientservice.utils.SecureTokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    ClientServiceImpl clientService;

    @Autowired
    private BuddyAdminService buddyAdminService;

    @Autowired
    private UserService userService;

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
        authentication.setId(sequenceGeneratorService.generateSequence(Authentication.SEQUENCE_NAME));
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

        Optional<Authentication> optionalAuthentication = authenticationRepository.findAuthenticationByToken(accessToken);

        if(optionalAuthentication.isPresent()) {
            Authentication authentication = optionalAuthentication.get();

            if(authentication.getUserType().equals(UserType.SUPER_ADMIN)) {
                Optional<BuddyAdmin> buddyAdmin = buddyAdminService.findOneByAuthenticationId(authentication.getId());
                if(buddyAdmin.isEmpty())
                    return Optional.empty();
                authentication.setEntityId(buddyAdmin.get().getId());
            } else if (authentication.getUserType().equals(UserType.CLIENT_ADMIN)) {
                Optional<Client> optionalClient = clientService.findByAuthenticationId(authentication.getId());
                if (optionalClient.isEmpty())
                    return Optional.empty();
                authentication.setEntityId(optionalClient.get().getId());
            } else if (authentication.getUserType().equals(UserType.USER)) {
                //Optional<User> optionalUser = userService.
                return Optional.empty();
            }
        }

        return Optional.empty();
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



    public Client registerClient(Authentication authentication) throws Exception {

        Example<Authentication> findByEmailExample = Example.of(Authentication.builder().userName(authentication.getUserName()).build());
        Optional<Authentication> optionalAuthentication =  findOneByExample(findByEmailExample);
        if(optionalAuthentication.isPresent())
            throw new EmailAlreadyRegistered("Email is already link with another user");

        Optional<Client> alreadyPresent = clientService.findClientByEmail(authentication.getUserName());
        if (alreadyPresent.isPresent()) {
            throw new EmailAlreadyRegistered();
        }
        try {

            Authentication registerUser = register(authentication);

            Client client = Client.builder().build();
            client.setRegisterDate(new java.sql.Date(System.currentTimeMillis()));
            client.setClientName(authentication.getName());
            client.setEmail(authentication.getUserName());
            client.setContactNumber(authentication.getMobileNumber());
            client.setAuthenticationId(registerUser.getId());

            return clientService.register(client);
        }catch (Exception exception) {
            logger.error(exception.getMessage(),exception);
            throw new Exception("Unable to register.Please try after some time.");
        }

    }

    @Override
    public BuddyAdmin registerBuddyAdmin(Authentication authentication) throws Exception {
        Example<Authentication> findByEmailExample = Example.of(Authentication.builder().userName(authentication.getUserName()).build());
        Optional<Authentication> optionalAuthentication =  findOneByExample(findByEmailExample);
        if(optionalAuthentication.isPresent())
            throw new EmailAlreadyRegistered("Email is already link with another user");


        Example<BuddyAdmin> findBuddyAdminByEmailExample = Example.of(BuddyAdmin.builder().email(authentication.getUserName()).build());
        Optional<BuddyAdmin> optionalBuddyAdmin = buddyAdminService.findBuddyAdminByEmail(authentication.getUserName());
        if(optionalBuddyAdmin.isPresent())
            throw new EmailAlreadyRegistered("Email is already link with another user");

        try{

            Authentication registerUser = register(authentication);

            BuddyAdmin buddyAdmin = BuddyAdmin.builder()
                    .name(authentication.getName())
                    .contactNumber(authentication.getMobileNumber())
                    .email(authentication.getUserName())
                    .authenticationId(registerUser.getId())
                    .build();
            buddyAdmin.setId(sequenceGeneratorService.generateSequence(BuddyAdmin.SEQUENCE_NAME));
            buddyAdmin.setCreatedTimestamp(new Date(System.currentTimeMillis()));

            return buddyAdminService.save(buddyAdmin);

        }catch (Exception exception) {
            logger.error(exception.getMessage(),exception);
            throw new Exception("Unable to register.Please try after some time.");
        }
    }


}

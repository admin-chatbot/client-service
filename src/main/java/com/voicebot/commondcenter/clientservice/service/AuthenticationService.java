package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.entity.Login;
import com.voicebot.commondcenter.clientservice.exception.InvalidUserNameAndPassword;
import com.voicebot.commondcenter.clientservice.exception.TokenNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthenticationService extends BaseService<Authentication> {

    Optional<Authentication> login(Login login) throws InvalidUserNameAndPassword;

    Authentication save(Authentication authentication);

    Authentication register(Authentication authentication);

    Authentication update(Authentication authentication);
    Optional<Authentication> authenticate(String accessToken) throws TokenNotFoundException;
    public Client registerClient(Authentication authentication) throws Exception;

    BuddyAdmin registerBuddyAdmin(Authentication authentication) throws Exception;

}

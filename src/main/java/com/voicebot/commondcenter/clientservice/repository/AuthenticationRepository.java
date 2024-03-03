package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthenticationRepository extends MongoRepository<Authentication, Long> {

    public Optional<Authentication> findAuthenticationByUserName(String email);

    public Optional<Authentication> findAuthenticationByToken(String token);
}

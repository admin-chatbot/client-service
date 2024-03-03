package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import com.voicebot.commondcenter.clientservice.entity.Client;
import com.voicebot.commondcenter.clientservice.exception.EmailAlreadyRegistered;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BuddyAdminService {

    List<BuddyAdmin> findAll();

    Optional<BuddyAdmin> findOne(Long id);

    BuddyAdmin save(BuddyAdmin buddyAdmin);

    BuddyAdmin register(Authentication authentication) throws Exception;

    Optional<BuddyAdmin> findOneByAuthenticationId(Long id);
}

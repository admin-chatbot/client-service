package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.Authentication;
import com.voicebot.commondcenter.clientservice.entity.BuddyAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BuddyAdminRepository extends MongoRepository<BuddyAdmin, Long> {

    Optional<BuddyAdmin> findBuddyAdminByAuthenticationId(Long authenticationId);

    Optional<BuddyAdmin> findBuddyAdminByEmail(String email);
}

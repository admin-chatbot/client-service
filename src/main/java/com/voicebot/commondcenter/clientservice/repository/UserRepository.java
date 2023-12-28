package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User,Long> {

    public List<User> findUsersByClientId(Long clientId);

    public Page<User> findUsersByClientId(Long clientId, Pageable pageable);

    public Optional<User> findUserByEmail(String email);

    public Optional<User> findUserByMobileNumber(String mobile);

    public Optional<User> findUserByEmpId(String empId);


}

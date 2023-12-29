package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.repository.UserRepository;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    @Override
    public List<User> findUsersByClientId(Long clientId) {
        return userRepository.findUsersByClientId(clientId);
    }

    @Override
    public Page<User> findUsersByClientId(Long clientId, Pageable pageable) {
        return userRepository.findUsersByClientId(clientId, pageable);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> findUserByMobile(String mobile) {
        return userRepository.findUserByMobileNumber(mobile);
    }

    @Override
    public User save(User user) {
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        user.setStatus(Status.ACTIVE);
        user.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        user.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Override
    public User edit(User user) {
        if (user!=null)
            user.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert user != null;
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByEmpId(String empId) {
        return userRepository.findUserByEmpId(empId);
    }
}

package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.UserSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public UserRepository getRepository();

    public List<User> findUsersByClientId(Long clientId);

    public Page<User> findUsersByClientId(Long clientId, Pageable pageable);

    public Optional<User> findUserByEmail(String email);

    public Optional<User> findUserByMobile(String mobile);

    public User save(User user);

    public User edit(User user);

    public Optional<User> findById(Long id);

    public Optional<User> findUserByEmpId(String empId);

    public List<User> search(UserSearchRequest userSearchRequest);
}

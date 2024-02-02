package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.UserSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.repository.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

     UserRepository getRepository();
     List<User> findUsersByClientId(Long clientId);
     Page<User> findUsersByClientId(Long clientId, Pageable pageable);
     Optional<User> findUserByEmail(String email);
     Optional<User> findUserByMobile(String mobile);
     User save(User user);
     User edit(User user);
     Optional<User> findById(Long id);
     Optional<User> findUserByEmpId(String empId);
     List<User> search(UserSearchRequest userSearchRequest);
     List<User> findByExample(Example<User> userExample);
}

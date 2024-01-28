package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.UserSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.User;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.filter.CriteriaBuilder;
import com.voicebot.commondcenter.clientservice.filter.SearchCriteria;
import com.voicebot.commondcenter.clientservice.repository.UserRepository;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    @Autowired
    private MongoTemplate mongoTemplate;

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



    @Override
    public List<User> search(UserSearchRequest userSearchRequest) {



        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();

        if(userSearchRequest==null)
            return null;

        if(userSearchRequest.getMobile() > 0)
            criteriaBuilder.addCriteria(new SearchCriteria("mobile","eq", userSearchRequest.getMobile(),""));

        System.out.println("Client Id: " +userSearchRequest.getClientId());

        if(userSearchRequest.getClientId()>0)
            criteriaBuilder.addCriteria(new SearchCriteria("clientId","eq", userSearchRequest.getClientId(),""));


        if(!StringUtils.isBlank(userSearchRequest.getEmpId())) {
            criteriaBuilder.addCriteria(new SearchCriteria("empId", "like", userSearchRequest.getEmpId(), ""));
        }
        if(!StringUtils.isBlank(userSearchRequest.getName())) {
            criteriaBuilder.addCriteria(new SearchCriteria("name", "like", userSearchRequest.getName(), ""));
        }
        if(!StringUtils.isBlank(userSearchRequest.getEmail())) {
            criteriaBuilder.addCriteria(new SearchCriteria("email", "like", userSearchRequest.getEmail(), ""));
        }

        if(!StringUtils.isBlank(userSearchRequest.getStatus()))
            criteriaBuilder.addCriteria(new SearchCriteria("status","eq",userSearchRequest.getStatus(),""));

        if(!StringUtils.isBlank(userSearchRequest.getAccess()))
            criteriaBuilder.addCriteria(new SearchCriteria("access","eq",userSearchRequest.getAccess(),""));

        Criteria root = criteriaBuilder.build();
        Query query
                = new Query(root);
        return mongoTemplate.find(query,User.class);
    }
    public List<User> findByExample(Example<User> userExample) {
        return userRepository.findAll(userExample);
    }
}

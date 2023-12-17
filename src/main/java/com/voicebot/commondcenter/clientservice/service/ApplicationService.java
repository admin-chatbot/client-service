package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ApplicationService {

    public ApplicationRepository repository();

    public Application onBoard(Application application);

    public List<Application> find();

    public Page<Application> find(Pageable pageable);

    public Optional<Application> findOne(Long id);


    public List<Application> findByClint(Long id);
    public Page<Application> findByClient(Pageable pageable, Long id);


}

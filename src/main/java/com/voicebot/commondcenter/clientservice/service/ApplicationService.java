package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.repository.ApplicationRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ApplicationService {

    ApplicationRepository repository();
    Application onBoard(Application application);
    Application edit(Application application);
    List<Application> find();
    Page<Application> find(Pageable pageable);
    Optional<Application> findOne(Long id);
    List<Application> findByClint(Long id);

    Optional<Application> findByClientAndId(Long clientId, Long id);

    Page<Application> findByClient(Pageable pageable, Long id);
    Optional<Application> findApplicationByName(String name);
    Optional<Application> findApplicationByClientAndName(Long clientId,String name);
    Page<Application> search(Application application,Pageable pageable);
    List<Application> search(Application application);
    List<Application> search(ApplicationSearchRequest applicationSearchRequest);


}

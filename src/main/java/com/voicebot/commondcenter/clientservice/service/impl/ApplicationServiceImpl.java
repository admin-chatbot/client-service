package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.filter.CriteriaBuilder;
import com.voicebot.commondcenter.clientservice.filter.FilterOperation;
import com.voicebot.commondcenter.clientservice.filter.SearchCriteria;
import com.voicebot.commondcenter.clientservice.repository.ApplicationRepository;
import com.voicebot.commondcenter.clientservice.service.ApplicationService;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.ast.NewExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService, BaseService<Application> {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public ApplicationRepository repository() {
        return applicationRepository;
    }

    @Override
    public Application onBoard(Application application) {
        application.setId(sequenceGeneratorService.generateSequence(Application.SEQUENCE_NAME));
        application.setRegisterDate(new Date(System.currentTimeMillis()));
        application.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        application.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        application.setStatus(Status.NEW);
        return applicationRepository.save(application);
    }

    @Override
    public Application edit(Application application) {
        if(application!=null)
            application.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert application != null;
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> find() {
        return applicationRepository.findAll();
    }

    @Override
    public Page<Application> find(Pageable pageable) {
        return applicationRepository.findAll(pageable);
    }



    @Override
    public Optional<Application> findOne(Long id) {
        return applicationRepository.findById(id);
    }



    @Override
    public List<Application> findByClint(Long id) {
        return applicationRepository.findApplicationsByClintId(id);
    }

    @Override
    public Optional<Application> findByClientAndId(Long clientId, Long id) {
        return applicationRepository.findApplicationByClintIdAndId(clientId,id);
    }

    @Override
    public Page<Application> findByClient(Pageable pageable, Long id) {
        return applicationRepository.findApplicationsByClintId(id,pageable);
    }

    @Override
    public Optional<Application> findApplicationByName(String name) {
        Example<Application> applicationExample = Example.of(Application.builder().name(name).build());
        return findOneByExample(applicationExample);
    }

    @Override
    public Optional<Application> findApplicationByClientAndName(Long clientId, String name) {
        System.out.println("From landing page in Application");
        Example<Application> applicationExample = Example.of(Application.builder().clintId(clientId).name(name).build());
        return findOneByExample(applicationExample);
    }

    @Override
    public List<Application> findByExample(Example<Application> applicationExample) {
        return applicationRepository.findAll(applicationExample);
    }

    @Override
    public Page<Application> findByExample(Example<Application> applicationExample, Pageable pageable) {
        return applicationRepository.findAll(applicationExample,pageable);
    }

    @Override
    public Optional<Application> findOneByExample(Example<Application> applicationExample) {
        return applicationRepository.findOne(applicationExample);
    }

    @Override
    public List<Application> search(Application application) {
        Criteria root = new Criteria();
        Criteria name = new Criteria("name").is(application.getName());
        Criteria date = new Criteria("").gt("").lt("");
        root.andOperator(name);
        Query query
                = new Query(root);
        return mongoTemplate.find(query,Application.class);
    }

    @Override
    public List<Application> search(ApplicationSearchRequest applicationSearchRequest) {



        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();

        if(applicationSearchRequest==null)
            return null;

        if(applicationSearchRequest.getClientId()!=null
                && applicationSearchRequest.getClientId() > 0)
            criteriaBuilder.addCriteria(new SearchCriteria("clintId","eq", applicationSearchRequest.getClientId(),""));

        if(!StringUtils.isBlank(applicationSearchRequest.getName())) {
            criteriaBuilder.addCriteria(new SearchCriteria("name", "like", applicationSearchRequest.getName(), ""));
        }
        if(!StringUtils.isBlank(applicationSearchRequest.getPurpose())) {
            criteriaBuilder.addCriteria(new SearchCriteria("purpose", "like", applicationSearchRequest.getPurpose(), ""));
        }
        if(!StringUtils.isBlank(applicationSearchRequest.getStatus()))
            criteriaBuilder.addCriteria(new SearchCriteria("status","eq",applicationSearchRequest.getStatus(),""));

        if(applicationSearchRequest.getFromDate()!=null
                && applicationSearchRequest.getToDate()!=null)
            criteriaBuilder.addCriteria(new SearchCriteria("registerDate","btn","",applicationSearchRequest.getFromDate(),applicationSearchRequest.getToDate()));

        Criteria root = criteriaBuilder.build();
        Query query
                = new Query(root);
        return mongoTemplate.find(query,Application.class);
    }

    private String toLikeRegex(String source) {
        return source.replaceAll("\\*", ".*");
    }

    @Override
    public Page<Application> search(Application application, Pageable pageable) {
        Example<Application> applicationExample = Example.of(application);
        return findByExample(applicationExample,pageable);
    }
}

package com.voicebot.commondcenter.clientservice.service.impl;



import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ServiceSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.enums.Status;
import com.voicebot.commondcenter.clientservice.filter.CriteriaBuilder;
import com.voicebot.commondcenter.clientservice.filter.SearchCriteria;
import com.voicebot.commondcenter.clientservice.repository.ServiceRepository;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.ServiceService;
import com.voicebot.commondcenter.clientservice.entity.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;




import java.util.Date;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService, BaseService<Service> {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceParameterServiceImpl serviceParameterService;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> find() {
        return serviceRepository.findAll();
    }

    @Override
    public com.voicebot.commondcenter.clientservice.entity.Service save(com.voicebot.commondcenter.clientservice.entity.Service service) {
        service.setId(sequenceGeneratorService.generateSequence(com.voicebot.commondcenter.clientservice.entity.Service.SEQUENCE_NAME));
        service.setStatus(Status.NEW);
        service.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        service.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        return serviceRepository.save(service);
    }

    @Override
    public com.voicebot.commondcenter.clientservice.entity.Service edit(com.voicebot.commondcenter.clientservice.entity.Service service) {
        if(service!=null)
            service.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert service != null;
        return serviceRepository.save(service);
    }

    @Override
    public Optional<com.voicebot.commondcenter.clientservice.entity.Service> fetchOne(Long serviceId) {
        return serviceRepository.findById(serviceId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findAllByClientId(Long clientId) {
        return serviceRepository.findServicesByClientId(clientId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findAllByApplicationId(Long applicationId) {
        return serviceRepository.findServiceByApplicationId(applicationId);
    }

    @Override
    public List<com.voicebot.commondcenter.clientservice.entity.Service> findServiceByClientIdAndKeywordLike(Long clientId, String keyword) {
        return serviceRepository.findServicesByClientIdAndKeywordLike(clientId, keyword);
    }




    @Override
    public List<Service> findByExample(Example<Service> serviceExample) {
        return serviceRepository.findAll(serviceExample);
    }



    @Override
    public Page<Service> findByExample(Example<Service> serviceExample, Pageable pageable) {
        return serviceRepository.findAll(serviceExample,pageable);
    }

    @Override
    public Optional<Service> findOneByExample(Example<Service> serviceExample) {
        return serviceRepository.findOne(serviceExample);
    }

    @Override
    public List<Service> search(Service service) {
        Criteria root = new Criteria();
        Criteria name = new Criteria("name").is(service.getName());
        Criteria method = new Criteria("method").is(service.getMethod());
        Criteria status = new Criteria("status").is(service.getStatus());
        Criteria endPoint = new Criteria("endPoint").is(service.getEndpoint());
        root.andOperator(name,method);
        Query query
                = new Query(root);
        return mongoTemplate.find(query,Service.class);
    }
    @Override
    public List<Service> search(ServiceSearchRequest serviceSearchRequest) {


        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();

        if(serviceSearchRequest==null)
            return null;

        if(serviceSearchRequest.getClientId()!=null
                && serviceSearchRequest.getClientId() > 0)
            criteriaBuilder.addCriteria(new SearchCriteria("clientId","eq", serviceSearchRequest.getClientId(),""));

        if(!StringUtils.isBlank(serviceSearchRequest.getName())) {
            criteriaBuilder.addCriteria(new SearchCriteria("name", "like", serviceSearchRequest.getName(), ""));
        }
        if(!StringUtils.isBlank(serviceSearchRequest.getEndPoint())) {
            criteriaBuilder.addCriteria(new SearchCriteria("endPoint", "like", serviceSearchRequest.getEndPoint(), ""));
        }
        if(!StringUtils.isBlank(serviceSearchRequest.getMethod()))
            criteriaBuilder.addCriteria(new SearchCriteria("method","like",serviceSearchRequest.getMethod(),""));

        if(!StringUtils.isBlank(serviceSearchRequest.getStatus()))
            criteriaBuilder.addCriteria(new SearchCriteria("status","like",serviceSearchRequest.getStatus(),""));

        Criteria root = criteriaBuilder.build();
        Query query
                = new Query(root);

        return mongoTemplate.find(query,Service.class);
    }

    private String toLikeRegex(String source) {
        return source.replaceAll("\\*", ".*");
    }

    @Override
    public Page<Service> search(Service service, Pageable pageable) {
        Example<Service> serviceExample = Example.of(service);
        return findByExample(serviceExample,pageable);
    }
}


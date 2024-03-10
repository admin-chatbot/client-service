package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.ServiceIntend;
import com.voicebot.commondcenter.clientservice.exception.ServiceNotFoundException;
import com.voicebot.commondcenter.clientservice.repository.ServiceIntendRepository;
import com.voicebot.commondcenter.clientservice.service.ServiceIntendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceIntendServiceImpl implements ServiceIntendService {

    @Autowired
    private ServiceIntendRepository  serviceIntendRepository;

    @Override
    public List<ServiceIntend> findByExample(Example<ServiceIntend> serviceIntendsExample) {
        return serviceIntendRepository.findAll(serviceIntendsExample);
    }

    @Override
    public Page<ServiceIntend> findByExample(Example<ServiceIntend> serviceIntendsExample, Pageable pageable) {
        return serviceIntendRepository.findAll(serviceIntendsExample,pageable);
    }

    @Override
    public Optional<ServiceIntend> findOneByExample(Example<ServiceIntend> serviceIntendsExample) {
        return serviceIntendRepository.findOne(serviceIntendsExample);
    }

    @Override
    public List<ServiceIntend> search(ServiceIntend serviceIntend) {
        Example<ServiceIntend> intendsExample  = Example.of(serviceIntend);
        return findByExample(intendsExample);
    }

    @Override
    public Page<ServiceIntend> search(ServiceIntend serviceIntend, Pageable pageable) {
        Example<ServiceIntend> intendsExample  = Example.of(serviceIntend);
        return findByExample(intendsExample,pageable);
    }

    @Override
    public ServiceIntend save(ServiceIntend serviceIntend) throws ServiceNotFoundException {
        return serviceIntendRepository.save(serviceIntend);
    }

    @Override
    public List<ServiceIntend> findAll() {
        return serviceIntendRepository.findAll();
    }

    @Override
    public List<ServiceIntend> findByName(String name) {
        return null;
    }

    @Override
    public List<ServiceIntend> findByServiceId(Long serviceId) {
        return serviceIntendRepository.findByServiceId(serviceId);
    }

    @Override
    public Optional<ServiceIntend> findByIntend(String intend) {
        Example<ServiceIntend> serviceIntendsExample = Example.of(ServiceIntend.builder().intend(intend).build());
        return findOneByExample(serviceIntendsExample);
    }

    @Override
    public List<ServiceIntend> findByApplicationId(Long applicationId) {
        return null;
    }

    @Override
    public ServiceIntend findById(Long id) throws ServiceNotFoundException {
        return serviceIntendRepository.findById(id).orElseThrow(() -> new ServiceNotFoundException("Service not found"));
    }

    @Override
    public ServiceIntend update(ServiceIntend serviceIntend) {
        return serviceIntendRepository.save(serviceIntend);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(ServiceIntend serviceIntend) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean existsById(Long id) {
        return serviceIntendRepository.existsById(id);
    }

    @Override
    public boolean existsByIntend(String name) {
        Example<ServiceIntend> serviceIntendsExample = Example.of(ServiceIntend.builder().intend(name).build());
        return findOneByExample(serviceIntendsExample).isPresent();
    }


}

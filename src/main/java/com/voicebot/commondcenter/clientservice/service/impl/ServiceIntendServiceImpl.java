package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.ServiceIntends;
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
    public List<ServiceIntends> findByExample(Example<ServiceIntends> serviceIntendsExample) {
        return null;
    }

    @Override
    public Page<ServiceIntends> findByExample(Example<ServiceIntends> serviceIntendsExample, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ServiceIntends> findOneByExample(Example<ServiceIntends> serviceIntendsExample) {
        return Optional.empty();
    }

    @Override
    public List<ServiceIntends> search(ServiceIntends serviceIntends) {
        return null;
    }

    @Override
    public Page<ServiceIntends> search(ServiceIntends serviceIntends, Pageable pageable) {
        return null;
    }

    @Override
    public ServiceIntends save(ServiceIntends serviceIntend) throws ServiceNotFoundException {
        return serviceIntendRepository.save(serviceIntend);
    }

    @Override
    public List<ServiceIntends> findAll() {
        return serviceIntendRepository.findAll();
    }

    @Override
    public List<ServiceIntends> findByName(String name) {
        return null;
    }

    @Override
    public List<ServiceIntends> findByServiceId(Long serviceId) {
        return serviceIntendRepository.findByServiceId(serviceId);
    }

    @Override
    public List<ServiceIntends> findByApplicationId(Long applicationId) {
        return null;
    }

    @Override
    public ServiceIntends findById(Long id) {
        return null;
    }

    @Override
    public ServiceIntends update(ServiceIntends serviceIntend) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(ServiceIntends serviceIntend) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public boolean existsByApplicationId(Long applicationId) {
        return false;
    }

    @Override
    public boolean existsByApplicationIdAndName(Long applicationId, String name) {
        return false;
    }
}

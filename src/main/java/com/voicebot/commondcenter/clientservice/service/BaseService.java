package com.voicebot.commondcenter.clientservice.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<I> {
    public List<I> findByExample(Example<I> iExample);

    public Page<I> findByExample(Example<I> iExample, Pageable pageable);

    public Optional<I> findOneByExample(Example<I> iExample);

    public List<I> search(I i);

    public Page<I> search(I i,Pageable pageable);


}

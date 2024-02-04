package com.voicebot.commondcenter.clientservice.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<I> {
    List<I> findByExample(Example<I> iExample);
    Page<I> findByExample(Example<I> iExample, Pageable pageable);
    Optional<I> findOneByExample(Example<I> iExample);
    List<I> search(I i);
    Page<I> search(I i,Pageable pageable);


}

package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.DocumentUpload;
import com.voicebot.commondcenter.clientservice.repository.DocumentUploadRepository;
import com.voicebot.commondcenter.clientservice.service.BaseService;
import com.voicebot.commondcenter.clientservice.service.DocumentUploadService;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentUploadServiceImpl implements DocumentUploadService, BaseService<DocumentUpload> {

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public DocumentUploadRepository getRepository() {
        return documentUploadRepository;
    }

    @Override
    public DocumentUpload onBoard(DocumentUpload documentUpload) {
        documentUpload.setId(sequenceGeneratorService.generateSequence(Application.SEQUENCE_NAME));
        return documentUploadRepository.save(documentUpload);
    }

    @Override
    public DocumentUpload edit(DocumentUpload documentUpload) {
        if(documentUpload!=null)
            documentUpload.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        assert documentUpload != null;
        return documentUploadRepository.save(documentUpload);
    }

    @Override
    public List<DocumentUpload> find() {
        return documentUploadRepository.findAll();
    }


    public Optional<DocumentUpload> findById(Long id) {
        return documentUploadRepository.findById(id);
    }

    @Override
    public List<DocumentUpload> findByExample(Example<DocumentUpload> documentUploadExample) {
        return null;
    }

    @Override
    public Page<DocumentUpload> findByExample(Example<DocumentUpload> documentUploadExample, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<DocumentUpload> findOneByExample(Example<DocumentUpload> documentUploadExample) {
        return Optional.empty();
    }

    @Override
    public List<DocumentUpload> search(DocumentUpload documentUpload) {
        return null;
    }

    @Override
    public Page<DocumentUpload> search(DocumentUpload documentUpload, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<DocumentUpload> findADocumentByClientIdAndFileName(Long id,String name) {
        Example<DocumentUpload> documentUploadExample = Example.of(DocumentUpload.builder().clientId(id).fileName(name).build());
        return findOneByExample(documentUploadExample);
    }

    @Override
    public List<DocumentUpload> findDocumentsByClientId(Long clientId) {
        return documentUploadRepository.findDocumentsByClientId(clientId);
    }

}

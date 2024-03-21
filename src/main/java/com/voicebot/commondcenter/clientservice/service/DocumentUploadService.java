package com.voicebot.commondcenter.clientservice.service;

import com.voicebot.commondcenter.clientservice.entity.DocumentUpload;
import com.voicebot.commondcenter.clientservice.repository.DocumentUploadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DocumentUploadService {

    DocumentUploadRepository getRepository();
    DocumentUpload onBoard(DocumentUpload documentUpload);
    DocumentUpload edit(DocumentUpload documentUpload);
    List<DocumentUpload> find();
    public Optional<DocumentUpload> findById(Long id);
    public Optional<DocumentUpload> findADocumentByClientIdAndFileName(Long id,String name);
    List<DocumentUpload> findDocumentsByClientId(Long clientId);


}

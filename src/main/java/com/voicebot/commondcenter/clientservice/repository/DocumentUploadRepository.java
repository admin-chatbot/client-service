package com.voicebot.commondcenter.clientservice.repository;

import com.voicebot.commondcenter.clientservice.entity.DocumentUpload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentUploadRepository extends MongoRepository<DocumentUpload,Long> {
    public Optional<DocumentUpload> findById(Long id);
    public List<DocumentUpload> findDocumentsByClientId(Long clientId);

}

package com.voicebot.commondcenter.clientservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Document(collection = "document")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUpload extends AbstractBaseEntity implements Serializable {

    @Id
    private Long id;

    @NotNull( message = "Client should not null or empty.")
    private Long clientId;

    @NotBlank( message = "File Name should not null or empty.")
    private String fileName;

    @NotBlank(message = "Description Name should not null or empty.")
    private String description;

    @NotBlank(message = "Category should not be null.")
    private String category;

    @NotBlank(message = "S3 Path should not be null.")
    private String s3Path;

}
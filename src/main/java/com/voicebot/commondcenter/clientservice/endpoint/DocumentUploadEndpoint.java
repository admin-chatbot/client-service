package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/s3/")
public class DocumentUploadEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentUploadEndpoint.class);


    @Autowired
    private S3Service service;

    @PostMapping(path = "upload/",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadFile(file).eTag());
    }

    @PostMapping(path = "/{bucket}/upload/",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    public ResponseEntity<?>  uploadFileOnBucket(@RequestPart(value = "file") MultipartFile file,@PathVariable(name = "bucket") String bucket) throws IOException {
        return ResponseEntity.ok(service.uploadFile(file,bucket).eTag());
    }


}

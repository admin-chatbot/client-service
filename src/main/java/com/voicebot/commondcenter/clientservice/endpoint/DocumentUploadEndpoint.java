package com.voicebot.commondcenter.clientservice.endpoint;

import com.voicebot.commondcenter.clientservice.entity.DocumentUpload;
import com.voicebot.commondcenter.clientservice.entity.Rule;
import com.voicebot.commondcenter.clientservice.service.DocumentUploadService;
import com.voicebot.commondcenter.clientservice.service.S3Service;
import com.voicebot.commondcenter.clientservice.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/s3/")
public class DocumentUploadEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentUploadEndpoint.class);


    @Autowired
    private S3Service service;

    @Autowired
    private DocumentUploadService documentUploadService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , name = "X-AUTH-LOG-HEADER"
                    , content = @Content(schema = @Schema(type = "string", defaultValue = ""))),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = DocumentUpload.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> onBoard(
            @RequestAttribute(name = "id") Long id,
            @RequestAttribute(name = "type") String type,
            @RequestParam("file") MultipartFile file,
            @RequestParam("clientId") Long clientId,
            @RequestParam("fileName") String fileName,
            @RequestParam("description") String description,
            @RequestParam("category") String category) {
        try {
            // Create a DocumentUpload instance
            DocumentUpload documentUpload = DocumentUpload.builder()
                    .clientId(clientId)
                    .fileName(fileName)
                    .description(description)
                    .category(category)
                    .build();
            Optional<DocumentUpload> alreadyPresent = documentUploadService.findADocumentByClientIdAndFileName(documentUpload.getClientId(), documentUpload.getFileName());
            if (alreadyPresent.isPresent()) {
                return ResponseBuilder.build400("Document is already onboarded.");
            }


            String s3Path = service.uploadFile(file, "DocumentPoc").eTag();
            documentUpload.setS3Path(service.uploadFile(file,"DocumentPoc").eTag());
            documentUpload.setS3Path(s3Path);

            // Save DocumentUpload to MongoDB
            DocumentUpload savedDocument = documentUploadService.onBoard(documentUpload);
            return ResponseBuilder.ok("Document successfully onboard.", savedDocument);
        } catch (Exception exception) {
            return ResponseBuilder.build500(exception);
        }
    }

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

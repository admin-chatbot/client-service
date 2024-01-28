package com.voicebot.commondcenter.clientservice.entity;



import com.voicebot.commondcenter.clientservice.discovery.service.model.Authorization;
import com.voicebot.commondcenter.clientservice.discovery.service.model.ResponseMessage;
import com.voicebot.commondcenter.clientservice.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "service")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "unq_name_method_endpoint",def = "{'endpoint': 1, 'method': 1,'name':1}", unique = true)
public class Service extends AbstractBaseEntity implements  Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "service_sequence";

    @Id
    private Long id;

    @NotBlank( message = "Endpoint should not null or empty.")
    private String endpoint;

    private String keyword;

    @NotBlank( message = "Method should not null or empty.")
    private String method; //GET/POST/PUT

    @NotBlank( message = "Name should not null or empty.")
    private String name;

    private String summary;

    private Authorization authorization;

    @NotNull( message = "Response Type should not null.")
    private List<String> responseType;

    private List<String> requestType;

    private List<ResponseMessage> responseForInvalidRequest;

    @NotNull( message = "Client Id should not null.")
    private Long clientId;

    @NotNull( message = "Application Id should not null.")
    private Long applicationId;

    private String botResponseTemplate;

    private Status status;

    private String responseSchema;
    private Long clintId;

    @Transient
    private int parameterCount;

    @Transient
    private ArrayList<ServiceParameter> serviceParameters;


}

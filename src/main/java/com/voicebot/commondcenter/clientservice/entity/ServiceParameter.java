package com.voicebot.commondcenter.clientservice.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "service_parameters")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "unq_name_in",def = "{ 'serviceId':1,'in': 1,'name':1}", unique = true)
public class ServiceParameter extends AbstractBaseEntity implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "serviceparameter_sequence";

    @Id
    private Long id;

    @NotNull( message = "Service Id should not null.")
    private long serviceId;

    @NotBlank( message = "Name should not null or empty.")
    private String name;

    private String description;

    private Boolean required;

    @NotBlank( message = "Type should not null or empty.")
    private String type;

    @NotBlank( message = "Param Type should not null or empty.")
    private String paramType;

    private String in;

    private String value;

    private String jsonFormat;

    private List<String> questionToGetInput;

    @Transient
    private Boolean questionAsked;

}

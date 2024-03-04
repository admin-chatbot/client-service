package com.voicebot.commondcenter.clientservice.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "service_intend")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ServiceIntends extends AbstractBaseEntity implements Serializable {

    private Long serviceId;

    private String intend;

    private List<String> questions;
}

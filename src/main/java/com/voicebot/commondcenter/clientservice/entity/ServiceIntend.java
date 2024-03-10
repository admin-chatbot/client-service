package com.voicebot.commondcenter.clientservice.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.List;

@Document(collection = "service_intend")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class ServiceIntend extends AbstractBaseEntity implements Serializable {

    @NotNull(message = "Service Id should not null or empty.")
    private Long serviceId;

    @NotNull(message = "Intend should not null or empty.")
    private String intend;

    @NotEmpty(message = "Please provide at least 1 question")
    private List<String> questions;
}

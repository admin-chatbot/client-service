package com.voicebot.commondcenter.clientservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "rule")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rule extends AbstractBaseEntity implements Serializable {

    @Id
    private Long id;

    @NotNull( message = "Client should not null or empty.")
    private Long clientId;

    @NotBlank(message = "Rule Name should not null or empty.")
    private String rulename;

    @NotNull(message = "Step id should not be null.")
    private Long stepId;

}
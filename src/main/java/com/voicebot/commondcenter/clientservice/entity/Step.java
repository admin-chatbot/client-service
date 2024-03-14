package com.voicebot.commondcenter.clientservice.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "step")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Step extends AbstractBaseEntity implements Serializable {

    @Id
    private Long id;

    @NotBlank(message = "Rule Name should not null or empty.")
    private String intent;

    @NotBlank(message = "Step id should not be null or empty.")
    private String action;

}
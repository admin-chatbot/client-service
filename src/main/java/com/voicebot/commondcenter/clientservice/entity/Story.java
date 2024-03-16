package com.voicebot.commondcenter.clientservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "story")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Story extends AbstractBaseEntity implements Serializable {
    @Id
    private Long id;

    @NotNull(message = "Rule Name should not null or empty.")
    private Long storyId;

    @NotNull(message = "Step id should not be null or empty.")
    private Long stepId;

    @NotNull( message = "Client should not null or empty.")
    private Long clientId;
}
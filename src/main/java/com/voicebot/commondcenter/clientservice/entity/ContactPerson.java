package com.voicebot.commondcenter.clientservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson extends AbstractBaseEntity implements  Serializable {
    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String number;
    private String designation;
}

package com.voicebot.commondcenter.clientservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson extends AbstractBaseEntity implements  Serializable {
    private String name;
    private String number;
    private String designation;
}

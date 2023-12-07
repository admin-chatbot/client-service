package com.voicebot.commondcenter.clientservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson implements BaseEntity, Serializable {
    private String name;
    private String number;
    private String designation;
}

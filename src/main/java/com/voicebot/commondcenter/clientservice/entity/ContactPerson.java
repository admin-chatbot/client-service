package com.voicebot.commondcenter.clientservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class ContactPerson {

    private String name;

    private String number;

    private String designation;
}

package com.voicebot.commondcenter.clientservice.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AbstractBaseEntity implements BaseEntity {

    public Timestamp createdTimestamp;

    public Timestamp modifiedTimestamp;
}

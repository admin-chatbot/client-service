package com.voicebot.commondcenter.clientservice.entity;

import lombok.Data;

import java.util.Date;


@Data
public class AbstractBaseEntity implements BaseEntity {

    public Date createdTimestamp;

    public Date modifiedTimestamp;
}

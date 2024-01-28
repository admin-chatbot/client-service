package com.voicebot.commondcenter.clientservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractBaseEntity implements BaseEntity {

    public Date createdTimestamp;

    public Date modifiedTimestamp;
}

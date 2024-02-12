package com.voicebot.commondcenter.clientservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractBaseEntity implements BaseEntity {

    public String createdUserId;

    public Date createdTimestamp;

    public String updatedUserId;

    public Date modifiedTimestamp;
}

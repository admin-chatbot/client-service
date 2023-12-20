package com.voicebot.commondcenter.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private int applicationCount;
    private int serviceCount;
    private List<ServiceCountDto> topUsed10Services;
    private List<ServiceCountDto> leastUsed10Services;

}

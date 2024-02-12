package com.voicebot.commondcenter.clientservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashBoardResponse {

          private  Map<
                String,Map<
                    String,Map<
                     String,Map<
                        String,LogsStatus
                            >
                        >
                    >
                > status ;
}

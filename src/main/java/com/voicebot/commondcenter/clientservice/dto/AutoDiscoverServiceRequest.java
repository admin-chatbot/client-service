package com.voicebot.commondcenter.clientservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class AutoDiscoverServiceRequest {

    @NotBlank(message = "Docs url should not null or empty.")
    private String url;


    private List<AutoDiscoverServiceRequestBody> autoDiscoverServiceRequestBodies;
}

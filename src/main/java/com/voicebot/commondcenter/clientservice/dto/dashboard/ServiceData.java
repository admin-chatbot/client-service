
package com.voicebot.commondcenter.clientservice.dto.dashboard;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "service",
    "logs"
})

public class ServiceData {

    @JsonProperty("service")
    private String service;
    @JsonProperty("logs")
    private Logs logs;





    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    public ServiceData withService(String service) {
        this.service = service;
        return this;
    }

    @JsonProperty("logs")
    public Logs getLogs() {
        return logs;
    }

    @JsonProperty("logs")
    public void setLogs(Logs logs) {
        this.logs = logs;
    }

    public ServiceData withLogs(Logs logs) {
        this.logs = logs;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ServiceData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}

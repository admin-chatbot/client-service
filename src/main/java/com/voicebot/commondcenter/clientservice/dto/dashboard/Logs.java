
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
    "SUCCESS",
    "FAIL"
})

public class Logs {

    @JsonProperty("SUCCESS")
    private Integer success;
    @JsonProperty("FAIL")
    private Integer fail;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("SUCCESS")
    public Integer getSuccess() {
        return success;
    }

    @JsonProperty("SUCCESS")
    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Logs withSuccess(Integer success) {
        this.success = success;
        return this;
    }

    public void successPlus1(){
        if(this.success==null)
            this.success = 0;
        this.success++;
    }

    public void failPlus1(){
        if(this.fail==null)
            this.fail = 0;
        this.fail++;
    }

    @JsonProperty("FAIL")
    public Integer getFail() {
        return fail;
    }

    @JsonProperty("FAIL")
    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Logs withFail(Integer fail) {
        this.fail = fail;
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

    public Logs withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}

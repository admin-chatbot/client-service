
package com.voicebot.commondcenter.clientservice.dto.dashboard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.models.auth.In;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "application",
    "data",
    "success",
    "fail"
})

public class ApplicationData {

    @JsonProperty("application")
    private String application;

    @JsonProperty("success")
    private Integer success;

    @JsonProperty("fail")
    private Integer fail;

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

    @JsonProperty("fail")
    public Integer getFail() {
        return fail;
    }

    @JsonProperty("fail")
    public void setFail(Integer fail) {
        this.fail = fail;
    }

    @JsonProperty("success")
    public Integer getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Integer success) {
        this.success = success;
    }

    @JsonProperty("data")
    private List<ServiceData> data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("application")
    public String getApplication() {
        return application;
    }

    @JsonProperty("application")
    public void setApplication(String application) {
        this.application = application;
    }

    public ApplicationData withApplication(String application) {
        this.application = application;
        return this;
    }

    @JsonProperty("data")
    public List<ServiceData> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<ServiceData> data) {
        this.data = data;
    }

    public ApplicationData withData(List<ServiceData> data) {
        this.data = data;
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

    public ApplicationData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}

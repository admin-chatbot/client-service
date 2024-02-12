
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "day",
    "name",
    "data",
    "success",
    "fail"
})

public class Weekly {

    @JsonProperty("day")
    private String day;
    @JsonProperty("name")
    private String name;
    @JsonProperty("data")
    private List<ApplicationData> data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

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

    @JsonProperty("day")
    public String getDay() {
        return day;
    }

    @JsonProperty("day")
    public void setDay(String day) {
        this.day = day;
    }

    public Weekly withDay(String day) {
        this.day = day;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Weekly withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("data")
    public List<ApplicationData> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<ApplicationData> data) {
        this.data = data;
    }

    public Weekly withData(List<ApplicationData> data) {
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

    public Weekly withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}

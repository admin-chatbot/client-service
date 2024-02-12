
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
    "Daily",
    "Monthly",
    "Weekly",
    "success",
    "fail"
})

public class ServiceLogs {

    @JsonProperty("Daily")
    private List<Daily> daily;
    @JsonProperty("Monthly")
    private List<Monthly> monthly;
    @JsonProperty("Weekly")
    private List<Weekly> weekly;

    @JsonProperty("success")
    private Integer success;

    @JsonProperty("fail")
    private Integer fail;

    @JsonProperty("fail")
    public int getFail() {
        return fail;
    }

    @JsonProperty("fail")
    public void setFail(int fail) {
        this.fail = fail;
    }

    @JsonProperty("success")
    public int getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(int success) {
        this.success = success;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("Daily")
    public List<Daily> getDaily() {
        return daily;
    }

    @JsonProperty("Daily")
    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    public ServiceLogs withDaily(List<Daily> daily) {
        this.daily = daily;
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

    @JsonProperty("Monthly")
    public List<Monthly> getMonthly() {
        return monthly;
    }

    @JsonProperty("Monthly")
    public void setMonthly(List<Monthly> monthly) {
        this.monthly = monthly;
    }

    public ServiceLogs withMonthly(List<Monthly> monthly) {
        this.monthly = monthly;
        return this;
    }

    @JsonProperty("Weekly")
    public List<Weekly> getWeekly() {
        return weekly;
    }

    @JsonProperty("Weekly")
    public void setWeekly(List<Weekly> weekly) {
        this.weekly = weekly;
    }

    public ServiceLogs withWeekly(List<Weekly> weekly) {
        this.weekly = weekly;
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

    public ServiceLogs withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}

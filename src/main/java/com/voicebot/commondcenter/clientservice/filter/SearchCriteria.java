package com.voicebot.commondcenter.clientservice.filter;

import lombok.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Data
public class SearchCriteria {
    private String key;
    private FilterOperation operation;
    private Object value;
    private Object[] betweenValues;
    private Collection<Object> collections;
    private String predicate;

    public SearchCriteria(String key, String operation, Object value, String predicate) {
        this.key = key;
        this.operation = FilterOperation.fromValue(operation);
        this.value = value;
        this.predicate = predicate;

    }

    public SearchCriteria(String key, String operation, String predicate,Object... betweenValues) {
        this.key = key;
        this.operation = FilterOperation.fromValue(operation);
        this.predicate = predicate;

        if(this.getOperation()==FilterOperation.BETWEEN) {
            this.betweenValues = new Object[2];
            this.betweenValues = betweenValues.clone();
        }

        if(this.getOperation()==FilterOperation.IN
                || this.getOperation()==FilterOperation.NOT_IN){
            this.collections = Arrays.stream(betweenValues).toList();
        }

    }



    public boolean isOrPredicate() {
        return predicate.equalsIgnoreCase("OR");
    }
}

package com.voicebot.commondcenter.clientservice.filter;

import com.voicebot.commondcenter.clientservice.filter.execption.NotImplementedException;

import java.util.stream.Stream;

public enum FilterOperation {
    EQUAL("eq"),
    NOT_EQUAL("neg"),
    GREATER_THAN("gt"),
    GREATER_THAN_OR_EQUAL_TO("gte"),
    LESS_THAN("lt"),
    LESSTHAN_OR_EQUAL_TO("lte"),
    IN("in"),
    NOT_IN("nin"),
    BETWEEN("btn"),
    CONTAINS("like"),
    START_WITH("sw"),
    END_WITH("nw");


    private String value;

    private FilterOperation(String value) {
        this.value = value;
    }

    public static FilterOperation fromValue(String value) {
        return Stream.of(values())
                .filter(op -> String.valueOf(op.value).equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(NotImplementedException::new);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

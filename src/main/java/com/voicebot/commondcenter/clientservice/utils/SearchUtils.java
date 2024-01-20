package com.voicebot.commondcenter.clientservice.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchUtils {
    //like
    //equal
    //notEqual
    //min
    //max
    //between
    //startWith
    //endWith


    public static List<String> chuck(String search) {
        Objects.requireNonNull(search, "Search should not null");
        return Arrays.stream(StringUtils.splitPreserveAllTokens(search,",")).toList();
    }

    public static Map<String,Object> fieldValueMap(List<String> strings) {
        return null;
    }
}

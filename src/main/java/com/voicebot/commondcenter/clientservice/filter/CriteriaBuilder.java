package com.voicebot.commondcenter.clientservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebot.commondcenter.clientservice.dto.ApplicationSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.Application;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CriteriaBuilder {
    private List<Criteria> criterias;

    public CriteriaBuilder() {
        this.criterias = new ArrayList<>();
    }
    public CriteriaBuilder addCriteria(SearchCriteria criteria) {
        switch (criteria.getOperation()) {
            case EQUAL
                    ->  criterias.add(new Criteria(criteria.getKey()).is(criteria.getValue()));
            case BETWEEN
                    -> criterias.add(new Criteria(criteria.getKey()).gte(criteria.getBetweenValues()[0]).lt(criteria.getBetweenValues()[1]));
            case IN
                    -> criterias.add(new Criteria(criteria.getKey()).in(criteria.getCollections()));
        }
        return this;
    }

    public Criteria build() {
        Criteria root = new Criteria();
        root.andOperator(criterias);
        return root;
    }


}

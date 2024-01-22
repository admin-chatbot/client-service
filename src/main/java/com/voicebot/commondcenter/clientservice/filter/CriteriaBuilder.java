package com.voicebot.commondcenter.clientservice.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilder {
    private final List<Criteria> criterias;

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
            case CONTAINS ->  {
                String regex = MongoRegexCreator.INSTANCE
                        .toRegularExpression((String) criteria.getValue(), MongoRegexCreator.MatchMode.CONTAINING);
                assert regex != null;
                criterias.add(new Criteria(criteria.getKey()).regex(regex));
            }
            case START_WITH
                    -> criterias.add(new Criteria(criteria.getKey()).regex("*"+criteria.getValue()));
        }
        return this;
    }

    public Criteria build() {
        Criteria root = new Criteria();
        root.andOperator(criterias);
        return root;
    }


}

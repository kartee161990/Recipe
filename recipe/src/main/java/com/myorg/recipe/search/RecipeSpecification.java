package com.myorg.recipe.search;

import com.myorg.recipe.entity.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
@EqualsAndHashCode
public class RecipeSpecification implements Specification<Recipe> {

    private final SearchCriteria searchCriteria;

    public RecipeSpecification(final SearchCriteria searchCriteria){
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var strToSearch = searchCriteria.getValue().toString().toLowerCase();
        var searchOperation = SearchOperation.getOperation(searchCriteria.getOperation());
        var recipeJoin = (searchCriteria.getFilterKey().equals("ingName")) ?
                root.join("ingredient") : root;

        if (searchOperation == null) {
            log.error("Unsupported operation : {}", searchCriteria.getOperation());
            throw new UnsupportedOperationException("Unsupported operation : " + searchCriteria.getOperation());
        }

        switch (searchOperation) {
            case CONTAINS:
                return cb.like(cb.lower(recipeJoin.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                return cb.notLike(cb.lower(recipeJoin.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case EQUAL:
                return (searchCriteria.getFilterKey().equals("ingName")) ?
                        cb.equal(cb.lower(recipeJoin.get(searchCriteria.getFilterKey())), strToSearch) :
                        cb.equal(recipeJoin.get(searchCriteria.getFilterKey()), strToSearch);

            case NOT_EQUAL:
                return cb.notEqual(cb.lower(recipeJoin.get(searchCriteria.getFilterKey())), strToSearch);

            case WITH:
                Boolean value = "true".equalsIgnoreCase(strToSearch) ? Boolean.TRUE :
                        "false".equalsIgnoreCase(strToSearch) ? Boolean.FALSE : null;
                return cb.equal(recipeJoin.get(searchCriteria.getFilterKey()), value);

            default:
                throw new RuntimeException("Operation not supported yet");
        }

    }


}

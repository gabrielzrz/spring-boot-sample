package br.com.gabrielzrz.repository.specification;

import br.com.gabrielzrz.util.LambdaUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Zorzi
 */
public abstract class BaseSpecification<T> {

    protected Predicate likeLowerCase(CriteriaBuilder cb, Root<T> root, String field, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    protected Predicate equalIgnoreCase(CriteriaBuilder cb, Root<T> root, String field, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return cb.equal(cb.lower(root.get(field)), value.toLowerCase());
    }

    protected <Y extends Comparable<? super Y>> Predicate greaterThanOrEqual( CriteriaBuilder cb, Root<T> root, String field, Y value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return cb.greaterThanOrEqualTo(root.get(field), value);
    }

    protected <Y extends Comparable<? super Y>> Predicate lessThanOrEqual(CriteriaBuilder cb, Root<T> root, String field, Y value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return cb.lessThanOrEqualTo(root.get(field), value);
    }

    protected <Y extends Comparable<? super Y>> Predicate between(CriteriaBuilder cb, Root<T> root, String field, Y from, Y to) {
        if (Objects.isNull(from) && Objects.isNull(to)) {
            return null;
        }
        if (Objects.nonNull(from) && Objects.nonNull(to)) {
            return cb.between(root.get(field), from, to);
        }
        if (Objects.nonNull(from)) {
            return cb.greaterThanOrEqualTo(root.get(field), from);
        }
        return cb.lessThanOrEqualTo(root.get(field), to);
    }

    protected Predicate equals(CriteriaBuilder cb, Root<T> root, String field, Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return cb.equal(root.get(field), value);
    }

    protected List<Predicate> removeNulls(List<Predicate> predicates) {
        return LambdaUtil.filter(predicates, Objects::nonNull);
    }
}

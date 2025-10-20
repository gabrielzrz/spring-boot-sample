package br.com.gabrielzrz.repository.specification;

import br.com.gabrielzrz.dto.request.filters.PersonFilterRequest;
import br.com.gabrielzrz.model.Person;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zorzi
 */
@Component
public class PersonSpecification extends BaseSpecification<Person> {
    public static Specification<Person> withFilters(PersonFilterRequest filters) {
        return (root, query, cb) -> {
            PersonSpecification spec = new PersonSpecification();

            List<Predicate> predicates = Arrays.asList(
                    spec.likeLowerCase(cb, root, "name", filters.name()),
                    spec.equalIgnoreCase(cb, root, "gender", filters.gender()),
                    spec.equals(cb, root, "enabled", filters.isEnabled()),
                    spec.between(cb, root, "birthDate", filters.birthDateFrom(), filters.birthDateTo())
            );
            List<Predicate> validPredicates = spec.removeNulls(predicates);
            return cb.and(validPredicates.toArray(new Predicate[0]));
        };
    }
}

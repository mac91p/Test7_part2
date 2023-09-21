package pl.kurs.personapp.services;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.kurs.personapp.models.Person;
import pl.kurs.personapp.respositories.predicates.IPersonPredicate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonSpecificationService {

    private List<IPersonPredicate> personPredicates;

    public PersonSpecificationService(List<IPersonPredicate> personPredicates) {
        this.personPredicates = personPredicates;
    }

    public Specification<Person> getCombinedSpecification(Map<String, String> queryParams) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (IPersonPredicate personPredicate : personPredicates) {
                if (!personPredicate.supportedParams(queryParams)) {
                    Predicate buildPredicate = null;
                    try {
                        buildPredicate = personPredicate.buildPredicate(root, criteriaBuilder, queryParams);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    predicates.add(buildPredicate);
                }
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        });
    }
}

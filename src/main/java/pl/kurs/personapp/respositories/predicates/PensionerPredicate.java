package pl.kurs.personapp.respositories.predicates;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import pl.kurs.personapp.models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PensionerPredicate implements IPersonPredicate{
    @Override
    public List<String> supportedParameters() {
        return List.of("pensionAmountFrom", "pensionAmountTo", "workedYearsFrom", "workedYearsTo");
    }

    @Override
    public Boolean supportedParams(Map<String, String> parameters) {
        return parameters.keySet().stream()
                .anyMatch(x -> !supportedParameters().contains(x));
    }

    @Override
    public Predicate buildPredicate(Root<Person> root, CriteriaBuilder cb, Map<String, String> queryParams) {

        List<Predicate> predicates = new ArrayList<>();
        if(queryParams.containsKey("pensionAmountFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("pensionAmount"), Double.valueOf(queryParams.get("pensionAmountFrom"))));
        }
        if(queryParams.containsKey("pensionAmountTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("pensionAmount"), Double.valueOf(queryParams.get("pensionAmountTo"))));
        }
        if(queryParams.containsKey("workedYearsFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("workedYears"), Double.valueOf(queryParams.get("workedYearsFrom"))));
        }
        if(queryParams.containsKey("workedYearsTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("workedYears"), Double.valueOf(queryParams.get("workedYearsTo"))));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

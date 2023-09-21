package pl.kurs.personapp.respositories.predicates;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import pl.kurs.personapp.models.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GeneralPredicate implements IPersonPredicate{
    @Override
    public List<String> supportedParameters() {
        return List.of("firstName", "lastName", "pesel", "sex", "heightInCmFrom", "heightInCmTo", "weightInKgFrom", "weightInKgTo",
                "emailAddress", "personType");
    }

    @Override
    public Boolean supportedParams(Map<String, String> parameters) {
        return parameters.keySet().stream()
                .anyMatch(x -> !supportedParameters().contains(x));
    }

    @Override
    public Predicate buildPredicate(Root<Person> root, CriteriaBuilder cb, Map<String, String> queryParams) {
        Expression<Integer> peselDigitAtIndex10 = cb.function("SUBSTRING", Integer.class,
                root.get("pesel"), cb.literal(10), cb.literal(1));

        Predicate malePredicate = cb.equal(cb.mod(peselDigitAtIndex10, 2), 1);
        Predicate femalePredicate = cb.equal(cb.mod(peselDigitAtIndex10, 2), 0);

        List<Predicate> predicates = new ArrayList<>();
        if(queryParams.containsKey("firstName")) {
            predicates.add(cb.equal((root.get("firstName")), queryParams.get("firstName")));
        }
        if(queryParams.containsKey("lastName")) {
            predicates.add(cb.equal(root.get("lastName"), queryParams.get("lastName")));
        }
        if(queryParams.containsKey("pesel")) {
            predicates.add(cb.equal(root.get("pesel"), queryParams.get("pesel")));
        }
        if(queryParams.containsKey("sex")) {
            if ("male".equalsIgnoreCase(queryParams.get("sex"))) {
                predicates.add(malePredicate);
            }
            if ("female".equalsIgnoreCase(queryParams.get("sex"))) {
                predicates.add(femalePredicate);
            }
        }
        if(queryParams.containsKey("emailAddress")) {
            predicates.add(cb.equal(root.get("emailAddress"), queryParams.get("emailAddress")));
        }
        if(queryParams.containsKey("personType")) {
            predicates.add(cb.equal(root.get("personType"), queryParams.get("personType")));
        }
        if(queryParams.containsKey("heightInCmFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("heightInCm"), Double.valueOf(queryParams.get("heightInCmFrom"))));
        }
        if(queryParams.containsKey("heightInCmTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("heightInCm"), Double.valueOf(queryParams.get("heightInCmTo"))));
        }
        if(queryParams.containsKey("weightInKgFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("weightInKg"), Double.valueOf(queryParams.get("weightInKgFrom"))));
        }
        if(queryParams.containsKey("weightInKgTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("weightInKg"), Double.valueOf(queryParams.get("weightInKgTo"))));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

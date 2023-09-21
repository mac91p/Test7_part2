package pl.kurs.personapp.respositories.predicates;

import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;
import pl.kurs.personapp.models.Person;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class EmployeePredicate implements IPersonPredicate {
    @Override
    public List<String> supportedParameters() {
        return List.of("salaryFrom", "salaryTo", "employmentStartFrom", "employmentStartTo", "currentPosition");
    }

    @Override
    public Boolean supportedParams(Map<String, String> parameters) {
        return parameters.keySet().stream()
                .anyMatch(x -> !supportedParameters().contains(x));
    }

    @Override
    public Predicate buildPredicate(Root<Person> root, CriteriaBuilder cb, Map<String, String> queryParams) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        if (queryParams.containsKey("currentPosition")) {
            predicates.add(cb.equal(cb.lower(root.get("currentPosition")), queryParams.get("currentPosition").toLowerCase(Locale.ROOT)));
        }
        if (queryParams.containsKey("salaryFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("currentSalary"), Double.valueOf(queryParams.get("salaryFrom"))));
        }
        if (queryParams.containsKey("salaryTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("currentSalary"), Double.valueOf(queryParams.get("salaryTo"))));
        }
        if (queryParams.containsKey("employmentStartFrom")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date employmentStartFrom = new Date(dateFormat.parse(queryParams.get("employmentStartFrom")).getTime());
            predicates.add(cb.greaterThanOrEqualTo(root.get("employmentStart"), employmentStartFrom));
        }
        if (queryParams.containsKey("employmentStartTo")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date employmentStartTo = new Date(dateFormat.parse(queryParams.get("employmentStartTo")).getTime());
            predicates.add(cb.greaterThanOrEqualTo(root.get("employmentStart"), employmentStartTo));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

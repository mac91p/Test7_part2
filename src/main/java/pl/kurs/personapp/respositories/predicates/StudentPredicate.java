package pl.kurs.personapp.respositories.predicates;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;
import pl.kurs.personapp.models.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class StudentPredicate implements IPersonPredicate {
    @Override
    public List<String> supportedParameters() {
        return List.of("universityName", "studyField", "yearOfStudiesFrom", "yearsOfStudiesTo", "scholarshipFrom", "scholarshipTo");
    }

    @Override
    public Boolean supportedParams(Map<String, String> parameters) {
        return parameters.keySet().stream()
                .anyMatch(x -> !supportedParameters().contains(x));
    }

    @Override
    public Predicate buildPredicate(Root<Person> root, CriteriaBuilder cb, Map<String, String> queryParams) {

        List<Predicate> predicates = new ArrayList<>();
        if(queryParams.containsKey("universityName")) {
            predicates.add(cb.equal(cb.lower(root.get("universityName")), queryParams.get("universityName").toLowerCase(Locale.ROOT)));
        }
        if(queryParams.containsKey("studyField")) {
            predicates.add(cb.equal(cb.lower(root.get("studyField")), queryParams.get("studyField").toLowerCase(Locale.ROOT)));
        }
        if(queryParams.containsKey("yearOfStudiesFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("yearOfStudies"), Double.valueOf(queryParams.get("yearOfStudiesFrom"))));
        }
        if(queryParams.containsKey("yearsOfStudiesTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("yearsOfStudies"), Double.valueOf(queryParams.get("yearsOfStudiesTo"))));
        }
        if(queryParams.containsKey("scholarshipFrom")) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("scholarship"), Double.valueOf(queryParams.get("scholarshipFrom"))));
        }
        if(queryParams.containsKey("scholarshipTo")) {
            predicates.add(cb.lessThanOrEqualTo(root.get("scholarship"), Double.valueOf(queryParams.get("scholarshipTo"))));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

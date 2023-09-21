package pl.kurs.personapp.respositories.predicates;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pl.kurs.personapp.models.Person;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IPersonPredicate {

    List<String> supportedParameters();
    Boolean supportedParams(Map<String,String> parameters);
    Predicate buildPredicate(Root<Person> root, CriteriaBuilder cb, Map<String,String> queryParams) throws ParseException;

}

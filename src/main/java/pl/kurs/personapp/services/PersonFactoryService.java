package pl.kurs.personapp.services;

import org.springframework.stereotype.Service;
import pl.kurs.personapp.exceptionhandling.exceptions.BadEntityException;
import pl.kurs.personapp.models.IPersonFactory;
import java.util.List;

@Service
public class PersonFactoryService {

    private List<IPersonFactory> personFactories;

    public PersonFactoryService(List<IPersonFactory> personFactories) {
        this.personFactories = personFactories;
    }

    public IPersonFactory getFactory(String type) {
        return personFactories.stream()
                .filter(x -> x.getSupportedType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new BadEntityException("No such entity"));
    }

}

package pl.kurs.personapp.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.personapp.exceptionhandling.exceptions.BadEntityException;
import pl.kurs.personapp.exceptionhandling.exceptions.BadIdException;
import pl.kurs.personapp.models.Person;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractGenericManagementService<T extends Person, R extends JpaRepository<T, Long>> implements IManagementService<T> {

    protected R repository;

    public AbstractGenericManagementService(R repository) {
        this.repository = repository;
    }

    @Override
    public T add(T entity) {
        return repository.save(
                Optional.ofNullable(entity)
                        .filter(x -> Objects.isNull(x.getId()))
                        .orElseThrow(() -> new BadEntityException("Bad entity for persist")));
    }


    @Override
    public T edit(T entity) {
        return repository.save(
                Optional.ofNullable(entity)
                        .filter(x -> Objects.nonNull(x.getId()))
                        .orElseThrow(() -> new BadIdException("Bad entity for update")));
    }

    @Override
    public T get(Long id) {
        return repository.findById(
                Optional.ofNullable(id).
                        filter(x -> x > 0).
                        orElseThrow(() -> new BadIdException("Invalid id"))
        ).orElseThrow(() -> new EntityNotFoundException("No entity with this id"));
    }

    @Override
    public List<T> saveAll(List<T> people) {
        return repository.saveAll(
                Optional.ofNullable(people)
                        .orElseThrow(() -> new IllegalArgumentException("Input list can not be empty")));
    }
}

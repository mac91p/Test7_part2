package pl.kurs.personapp.services;

import java.util.List;

public interface IManagementService<T> {

    T add(T entity);
    T edit(T entity);
    T get(Long id);
    List<T> saveAll(List<T> people);

}

package pl.kurs.personapp.services;

import org.springframework.stereotype.Service;
import pl.kurs.personapp.exceptionhandling.exceptions.BadEntityException;
import pl.kurs.personapp.models.ImportStatus;
import pl.kurs.personapp.respositories.ImportStatusRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImportStatusManagementService {

    private ImportStatusRepository repository;

    public ImportStatusManagementService(ImportStatusRepository repository) {
        this.repository = repository;
    }

    public ImportStatus add(ImportStatus entity) {
        return repository.save(
                Optional.ofNullable(entity)
                        .filter(x -> Objects.isNull(x.getId()))
                        .orElseThrow(() -> new BadEntityException("Bad entity for persist")));
    }

    public List<ImportStatus> getAll() {
        return repository.findAll();
    }
}

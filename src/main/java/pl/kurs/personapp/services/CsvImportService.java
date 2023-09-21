package pl.kurs.personapp.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.personapp.models.IPersonFactory;
import pl.kurs.personapp.models.ImportStatus;
import pl.kurs.personapp.models.Person;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@Service
public class CsvImportService {

    private final Semaphore importSemaphore = new Semaphore(1);
    private final PersonFactoryService personFactoryService;
    private final PersonManagementService personManagementService;
    private ImportStatus importStatus;

    public CsvImportService(PersonFactoryService personFactoryService, PersonManagementService personManagementService, ImportStatus importStatus) {
        this.personFactoryService = personFactoryService;
        this.personManagementService = personManagementService;
        this.importStatus = importStatus;
    }

    @Async
    public CompletableFuture<Void> importCsvData(MultipartFile file) throws ParseException {
        try {
            importSemaphore.acquire();
            List<Person> people = new ArrayList<>();
            importStatus.setState(ImportStatus.State.RUNNING);
            importStatus.setStartTime(Instant.now());
            importStatus.setProcessedRows(0);
            try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                String[] row;
                while ((row = reader.readNext()) != null) {
                    if (row.length > 0) {
                        String type = row[0];
                        IPersonFactory matchingFactory = personFactoryService.getFactory(type);
                        Person person = matchingFactory.createPersonFromCsvRow(row);
                        if (person != null) {
                            people.add(person);
                            importStatus.setProcessedRows(importStatus.getProcessedRows() + 1);
                        }
                    }
                }
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
                importStatus.setState(ImportStatus.State.FAILED);
                importStatus.setEndTime(Instant.now());
            }
            personManagementService.saveAll(people);
            importStatus.setState(ImportStatus.State.COMPLETED);
            importStatus.setEndTime(Instant.now());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            importSemaphore.release();
        }
        return null;
    }


    public ImportStatus getImportStatus() {
        return importStatus;
    }
}
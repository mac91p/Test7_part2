package pl.kurs.personapp.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.personapp.exceptionhandling.exceptions.ImportLockedException;
import pl.kurs.personapp.models.IPersonFactory;
import pl.kurs.personapp.models.ImportStatus;
import pl.kurs.personapp.models.Person;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private final PersonFactoryService personFactoryService;
    private final PersonManagementService personManagementService;
    private final ImportStatus importStatus;
    private final ImportStatusManagementService importStatusManagementService;
    private final ImportLockService importLockService;

    public CsvImportService(PersonFactoryService personFactoryService, PersonManagementService personManagementService,
                            ImportStatus importStatus, ImportStatusManagementService importStatusManagementService,
                            ImportLockService importLockService) {
        this.personFactoryService = personFactoryService;
        this.personManagementService = personManagementService;
        this.importStatus = importStatus;
        this.importStatusManagementService = importStatusManagementService;
        this.importLockService = importLockService;
    }

    public void importCsvData(MultipartFile file) {

        if (importLockService.tryAcquireImportLock()) {
            try {
                Runnable importRunnable = () -> {

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
                    } catch (IOException | CsvValidationException | ParseException e) {
                        e.printStackTrace();
                        importStatus.setState(ImportStatus.State.FAILED);
                        importStatus.setEndTime(Instant.now());
                        importStatusManagementService.add(importStatus);
                    }
                    personManagementService.saveAll(people);
                    importStatus.setState(ImportStatus.State.COMPLETED);
                    importStatus.setEndTime(Instant.now());
                    importStatusManagementService.add(importStatus);
                    importLockService.releaseImportLock();
                };
                Thread importThread = new Thread(importRunnable);
                importThread.start();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            throw new ImportLockedException("Import is already in progress");
        }
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }
}
package pl.kurs.personapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.personapp.dto.EmployeeDto;
import pl.kurs.personapp.dto.PersonDto;
import pl.kurs.personapp.models.*;
import pl.kurs.personapp.services.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private ModelMapper mapper;
    private ObjectMapper objectMapper;
    private PersonManagementService personManagementService;
    private PersonFactoryService personFactoryService;
    private EmployeePositionsService employeePositionsService;
    private CsvImportService csvImportService;

    public PersonController(ModelMapper mapper, ObjectMapper objectMapper, PersonManagementService personManagementService,
                            PersonFactoryService personFactoryService, EmployeePositionsService employeePositionsService,
                            CsvImportService csvImportService) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.personManagementService = personManagementService;
        this.personFactoryService = personFactoryService;
        this.employeePositionsService = employeePositionsService;
        this.csvImportService = csvImportService;
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody JsonNode jsonNode) throws ParseException {
        IPersonFactory matchingFactory = personFactoryService.getFactory(jsonNode.get("personType").asText());
        Person newPerson = matchingFactory.createPerson(jsonNode);
        personManagementService.add(newPerson);
        PersonDto personDto = mapper.map(newPerson, matchingFactory.createPersonDto(newPerson));
        return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
    }

    @GetMapping
    public ResponseEntity<PageImpl<PersonDto>> getPeople(@RequestParam Map<String, String> parameters, @PageableDefault Pageable pageable) {
        List<Person> allPeople = personManagementService.getAllPeople(parameters);
        List<PersonDto> allPeopleDto = allPeople.stream()
                .map(x -> {
                    IPersonFactory personFactory = personFactoryService.getFactory(x.getPersonType());
                    return mapper.map(x, personFactory.createPersonDto(x));
                })
                .collect(Collectors.toList());
        PageImpl<PersonDto> personDtoPage = new PageImpl<>(allPeopleDto, pageable, allPeople.size());
        return ResponseEntity.status(HttpStatus.OK).body(personDtoPage);
    }

    @PutMapping
    public ResponseEntity<PersonDto> updatePerson(@RequestBody Map<String, String> personToUpdate) {
        Person personForUpdate = personManagementService.get(Long.parseLong(personToUpdate.get("id")));
        Person updatedPerson = mapper.map(personToUpdate, personForUpdate.getClass());
        IPersonFactory matchingFactory = personFactoryService.getFactory(personToUpdate.get("personType"));
        personManagementService.edit(updatedPerson);
        PersonDto personDto = mapper.map(updatedPerson, matchingFactory.createPersonDto(updatedPerson));
        return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
    }


    @PutMapping(value = "position/{id}")
    public ResponseEntity<EmployeeDto> manageEmployeePositions(@PathVariable("id") Long id, @RequestBody Map<String, String> positionUpdate) {
        Employee employee = (Employee) personManagementService.get(id);
        Position position = mapper.map(positionUpdate, Position.class);
        Employee editedEmployee = employeePositionsService.updatePositions(employee, position);
        personManagementService.edit(editedEmployee);
        System.out.println(position);
        EmployeeDto employeeDto = mapper.map(editedEmployee, EmployeeDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeDto);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile inputFile) {
        try {
            CompletableFuture<Void> importFuture = csvImportService.importCsvData(inputFile);
            importFuture.get();
            return ResponseEntity.ok("Import started");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import failed");
    }

    @GetMapping("/progress")
    public ResponseEntity<ImportStatus> getCurrentImportState() {
        ImportStatus currentStatus = csvImportService.getImportStatus();
        return ResponseEntity.ok(currentStatus);

    }

}




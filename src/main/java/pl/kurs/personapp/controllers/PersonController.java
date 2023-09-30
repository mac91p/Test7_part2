package pl.kurs.personapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.personapp.dto.EmployeeDto;
import pl.kurs.personapp.dto.ImportStatusDto;
import pl.kurs.personapp.dto.ImportStatusSimpleDto;
import pl.kurs.personapp.dto.PersonDto;
import pl.kurs.personapp.models.*;
import pl.kurs.personapp.services.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private ModelMapper mapper;
    private PersonManagementService personManagementService;
    private PersonFactoryService personFactoryService;
    private EmployeePositionsService employeePositionsService;
    private CsvImportService csvImportService;
    private ImportStatusManagementService importStatusManagementService;

    public PersonController(ModelMapper mapper, PersonManagementService personManagementService,
                            PersonFactoryService personFactoryService, EmployeePositionsService employeePositionsService,
                            CsvImportService csvImportService, ImportStatusManagementService importStatusManagementService) {
        this.mapper = mapper;
        this.personManagementService = personManagementService;
        this.personFactoryService = personFactoryService;
        this.employeePositionsService = employeePositionsService;
        this.csvImportService = csvImportService;
        this.importStatusManagementService = importStatusManagementService;
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
    public ResponseEntity<ImportStatusSimpleDto> importCsv(@RequestParam("file") MultipartFile inputFile) {
        try {
            csvImportService.importCsvData(inputFile);
            ImportStatus importStatus = csvImportService.getImportStatus();
            ImportStatusSimpleDto statusSimpleDto = mapper.map(importStatus, ImportStatusSimpleDto.class);
            return ResponseEntity.ok(statusSimpleDto);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        ImportStatus importStatus = csvImportService.getImportStatus();
        ImportStatusSimpleDto statusSimpleDto = mapper.map(importStatus, ImportStatusSimpleDto.class);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(statusSimpleDto);
    }

    @GetMapping("/progress")
    public ResponseEntity<ImportStatusDto> getCurrentImportState() {
        ImportStatus currentStatus = csvImportService.getImportStatus();
        ImportStatusDto importStatusDto = mapper.map(currentStatus, ImportStatusDto.class);
        return ResponseEntity.ok(importStatusDto);
    }

    @GetMapping("/history")
    public ResponseEntity<PageImpl<ImportStatusDto>> getStatuses(@PageableDefault Pageable pageable) {
        List<ImportStatus> importStatusesList = importStatusManagementService.getAll();
        List<ImportStatusDto> importStatusDtos = importStatusesList.stream()
                .map(x -> mapper.map(x, ImportStatusDto.class))
                .collect(Collectors.toList());
        PageImpl<ImportStatusDto> personDtoPage = new PageImpl<>(importStatusDtos, pageable, importStatusesList.size());
        return ResponseEntity.status(HttpStatus.OK).body(personDtoPage);
    }

}




package pl.kurs.personapp.models;

import com.fasterxml.jackson.databind.JsonNode;
import pl.kurs.personapp.dto.PersonDto;
import java.text.ParseException;


public interface IPersonFactory {

    Person createPerson(JsonNode jsonNode) throws ParseException;
    String getSupportedType();
    Class<? extends PersonDto> createPersonDto(Person person);
    Person createPersonFromCsvRow(String[] csvRow) throws ParseException;






}

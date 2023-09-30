package pl.kurs.personapp.dto;

import pl.kurs.personapp.models.ImportStatus;

public class ImportStatusSimpleDto {

    private ImportStatus.State state;

    public ImportStatus.State getState() {
        return state;
    }

    public void setState(ImportStatus.State state) {
        this.state = state;
    }
}

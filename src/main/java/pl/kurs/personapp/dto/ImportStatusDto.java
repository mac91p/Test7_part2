package pl.kurs.personapp.dto;

import pl.kurs.personapp.models.ImportStatus;
import java.time.Instant;

public class ImportStatusDto {


    private Long id;
    private ImportStatus.State state;
    private Instant startTime;
    private Instant endTime;
    private int processedRows;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImportStatus.State getState() {
        return state;
    }

    public void setState(ImportStatus.State state) {
        this.state = state;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public int getProcessedRows() {
        return processedRows;
    }

    public void setProcessedRows(int processedRows) {
        this.processedRows = processedRows;
    }
}

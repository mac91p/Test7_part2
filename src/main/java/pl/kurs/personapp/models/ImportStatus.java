package pl.kurs.personapp.models;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ImportStatus {

    private State state;
    private Instant startTime;
    private Instant endTime;
    private int processedRows;


    public State getState() {
        return state;
    }

    public void setState(State state) {
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

    public enum State {
        RUNNING,
        COMPLETED,
        FAILED
    }




}

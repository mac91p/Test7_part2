package pl.kurs.personapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.kurs.personapp.models.Position;

import java.sql.Date;
import java.util.Set;

public class EmployeeDto extends PersonDto {

    private String currentPosition;
    private double currentSalary;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date employmentStart;

    private Set<Position> positions;

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public double getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(double currentSalary) {
        this.currentSalary = currentSalary;
    }

    public Date getEmploymentStart() {
        return employmentStart;
    }

    public void setEmploymentStart(Date employmentStart) {
        this.employmentStart = employmentStart;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }
}

package pl.kurs.personapp.dto;

import java.sql.Date;
public class PositionDto {

    private Long id;
    private String name;
    private double salary;
    private Date startOfWork;
    private Date endOfWork;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getStartOfWork() {
        return startOfWork;
    }

    public void setStartOfWork(Date startOfWork) {
        this.startOfWork = startOfWork;
    }

    public Date getEndOfWork() {
        return endOfWork;
    }

    public void setEndOfWork(Date endOfWork) {
        this.endOfWork = endOfWork;
    }
}

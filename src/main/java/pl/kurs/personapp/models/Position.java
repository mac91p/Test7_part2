package pl.kurs.personapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id_position")
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private double salary;
    @Version
    private Long version;
    private Date startOfWork;
    private Date endOfWork;


    public Position() {
    }

    public Position(@NotEmpty String name, @NotNull double salary, Date startOfWork, Date endOfWork) {
        this.name = name;
        this.salary = salary;
        this.startOfWork = startOfWork;
        this.endOfWork = endOfWork;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.salary, salary) == 0 && Objects.equals(id, position.id) && Objects.equals(name, position.name) && Objects.equals(version, position.version) && Objects.equals(startOfWork, position.startOfWork) && Objects.equals(endOfWork, position.endOfWork);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, version, startOfWork, endOfWork);
    }

}

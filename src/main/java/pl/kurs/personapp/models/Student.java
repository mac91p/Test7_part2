package pl.kurs.personapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student extends Person implements Serializable {
    @NotBlank
    private String universityName;
    @NotNull
    private double yearOfStudies;
    @NotBlank
    private String studyField;
    @NotNull
    private double scholarship;


    public Student() {
    }

    public Student(String personType, String firstName, String lastName, String pesel, double heightInCm, double weightInKg, String emailAddress, String universityName, double yearOfStudies, String studyField, double scholarship) {
        super(personType, firstName, lastName, pesel, heightInCm, weightInKg, emailAddress);
        this.universityName = universityName;
        this.yearOfStudies = yearOfStudies;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public double getYearOfStudies() {
        return yearOfStudies;
    }

    public void setYearOfStudies(double yearOfStudies) {
        this.yearOfStudies = yearOfStudies;
    }

    public String getStudyField() {
        return studyField;
    }

    public void setStudyField(String studyField) {
        this.studyField = studyField;
    }

    public double getScholarship() {
        return scholarship;
    }

    public void setScholarship(double scholarship) {
        this.scholarship = scholarship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Double.compare(student.yearOfStudies, yearOfStudies) == 0 && Double.compare(student.scholarship, scholarship) == 0 && Objects.equals(universityName, student.universityName) && Objects.equals(studyField, student.studyField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), universityName, yearOfStudies, studyField, scholarship);
    }

    @Override
    public String toString() {
        return  super.toString() +
                "universityName='" + universityName + '\'' +
                ", yearOfStudies=" + yearOfStudies +
                ", studyField='" + studyField + '\'' +
                ", scholarship=" + scholarship +
                '}';
    }
}

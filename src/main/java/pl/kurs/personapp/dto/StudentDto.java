package pl.kurs.personapp.dto;

public class StudentDto extends PersonDto {

    private String universityName;
    private double yearOfStudies;
    private String studyField;
    private double scholarship;

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
}

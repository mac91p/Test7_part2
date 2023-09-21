package pl.kurs.personapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pensioners")
public class Pensioner extends Person implements Serializable {
    @NotNull
    private double pensionAmount;
    @NotNull
    private double workedYears;


    public Pensioner() {
    }

    public Pensioner(String personType, String firstName, String lastName, String pesel, double heightInCm, double weightInKg, String emailAddress, double pensionAmount, double workedYears) {
        super(personType, firstName, lastName, pesel, heightInCm, weightInKg, emailAddress);
        this.pensionAmount = pensionAmount;
        this.workedYears = workedYears;
    }

    public double getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(double pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public double getWorkedYears() {
        return workedYears;
    }

    public void setWorkedYears(double workedYears) {
        this.workedYears = workedYears;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pensioner pensioner = (Pensioner) o;
        return Double.compare(pensioner.pensionAmount, pensionAmount) == 0 && Double.compare(pensioner.workedYears, workedYears) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pensionAmount, workedYears);
    }
}

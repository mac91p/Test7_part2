package pl.kurs.personapp.dto;

public class PensionerDto extends PersonDto {


    private double pensionAmount;
    private double workedYears;

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
}

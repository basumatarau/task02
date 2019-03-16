package by.htp.basumatarau.jdbc.dao.beans;

import java.util.Objects;

public class RegisteredEmployee {
    private int id;
    private String jobPosition;
    private int fidEmployee;
    private int fidAddress;
    private int fidCompany;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public int getFidEmployee() {
        return fidEmployee;
    }

    public void setFidEmployee(int fidEmployee) {
        this.fidEmployee = fidEmployee;
    }

    public int getFidAddress() {
        return fidAddress;
    }

    public void setFidAddress(int fidAddress) {
        this.fidAddress = fidAddress;
    }

    public int getFidCompany() {
        return fidCompany;
    }

    public void setFidCompany(int fidCompany) {
        this.fidCompany = fidCompany;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredEmployee that = (RegisteredEmployee) o;
        return id == that.id &&
                fidEmployee == that.fidEmployee &&
                fidAddress == that.fidAddress &&
                fidCompany == that.fidCompany &&
                Objects.equals(jobPosition, that.jobPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobPosition, fidEmployee, fidAddress, fidCompany);
    }
}

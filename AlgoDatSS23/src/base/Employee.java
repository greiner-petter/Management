package de.ostfalia.aud.s23ss.base;

import java.time.LocalDate;

public class Employee implements IEmployee {
    private final int key;
    private final LocalDate birthdate;
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final LocalDate hiredate;
    private final Department department;

    public Employee(String input) {
        String[] splitInput = input.split(";");
        this.key = Integer.parseInt(splitInput[0].trim());
        this.birthdate = LocalDate.parse(splitInput[1].trim());
        this.firstName = splitInput[2].trim();
        this.lastName = splitInput[3].trim();
        this.gender = Gender.valueOf(splitInput[4].toUpperCase().trim());
        this.hiredate = LocalDate.parse(splitInput[5].trim());
        this.department = Department.valueOf(splitInput[6].toUpperCase().trim());
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getName() {
        return lastName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public LocalDate getBirthdate() {
        return birthdate;
    }

    @Override
    public LocalDate getHiredate() {
        return hiredate;
    }

    @Override
    public Department getDepartment() {
        return department;
    }

    @Override
    public int compareTo(IEmployee o) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(";");
        sb.append(birthdate);
        sb.append(";");
        sb.append(firstName);
        sb.append(";");
        sb.append(lastName);
        sb.append(";");
        sb.append(gender);
        sb.append(";");
        sb.append(hiredate);
        sb.append(";");
        sb.append(department.toString());
        return sb.toString();
    }
}

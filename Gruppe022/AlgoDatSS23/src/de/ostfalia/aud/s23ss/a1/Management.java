package de.ostfalia.aud.s23ss.a1;


import de.ostfalia.aud.s23ss.base.Department;
import de.ostfalia.aud.s23ss.base.Employee;
import de.ostfalia.aud.s23ss.base.IEmployee;
import de.ostfalia.aud.s23ss.base.IManagement;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Management implements IManagement {
    private IEmployee[] data = new IEmployee[8];
    private int operations;

    public Management(String fileName) throws IOException {
        operations = 0;
        FileReader fileReader = new FileReader(fileName);
        Scanner scan = new Scanner(fileReader);
        scan.nextLine();
        int i = 0;
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            data[i] = new Employee(nextLine);
            i++;
            if (i == data.length) {
                data = Arrays.copyOf(data, data.length + 1);
            }
            operations++;
        }
        clearEmptyFields();
        fileReader.close();
        scan.close();
    }

    public Management(String[] input) {
        int i = 0;
        for (String employee : input) {
            data[i] = new Employee(employee);
            i++;
            if (i == data.length) {
                data = Arrays.copyOf(data, data.length + 1);
            }
        }
        clearEmptyFields();
    }

    public Management() {
        data = new IEmployee[0];
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public void insert(IEmployee member) {
        operations = 0;
        data = Arrays.copyOf(data, data.length + 1);
        data[data.length - 1] = new Employee(member.toString());
        operations++;
    }

    @Override
    public IEmployee search(int key) {
        operations = 0;
        for (IEmployee employee : data) {
            if (employee.getKey() == key) {
                return employee;
            }
            operations++;
        }
        return null;
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        operations = 0;
        IEmployee[] matchingEmployees = new IEmployee[1];
        int i = 0;
        for (IEmployee employee : data) {
            if (employee.getName().equals(name) && employee.getFirstName().equals(firstName)) {
                if (i == matchingEmployees.length - 1) {
                    matchingEmployees = Arrays.copyOf(matchingEmployees, matchingEmployees.length + 1);
                }
                matchingEmployees[i] = employee;
                i++;
            }
            operations++;
        }
        matchingEmployees = Arrays.copyOf(matchingEmployees, matchingEmployees.length - 1);
        return matchingEmployees;
    }

    @Override
    public int size(Department department) {
        operations = 0;
        int counter = 0;
        for (IEmployee employee : data) {
            if (employee.getDepartment() == department) {
                counter++;
            }
            operations++;
        }
        return counter;
    }

    @Override
    public IEmployee[] members(Department department) {
        operations = 0;
        IEmployee[] matchingEmployees = new IEmployee[0];
        int i = 0;
        for (IEmployee employee : data) {
            if (employee.getDepartment() == department) {
                if (i == matchingEmployees.length) {
                    matchingEmployees = Arrays.copyOf(matchingEmployees, matchingEmployees.length + 1);
                }
                matchingEmployees[i] = employee;
                i++;
                operations++;
            }
        }
        return matchingEmployees;
    }

    @Override
    public IEmployee[] toArray() {
        return data;
    }

    @Override
    public int numberOfOperations() {
        return operations;
    }

    @Override
    public int height() {
        return 0;
    }

    public void clearEmptyFields() {
        for (IEmployee employee : data) {
            if (employee == null) {
                data = Arrays.copyOf(data, data.length - 1);
            }
        }
    }
}

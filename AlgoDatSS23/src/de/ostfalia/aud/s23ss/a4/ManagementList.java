package de.ostfalia.aud.s23ss.a4;

import de.ostfalia.aud.s23ss.base.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ManagementList implements IManagement {
    private final ArrayList<IEmployee> data = new ArrayList<>();

    public ManagementList(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        Scanner scan = new Scanner(fileReader);
        scan.nextLine();
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            data.add(new Employee(nextLine));
        }
        fileReader.close();
        scan.close();
    }

    public ManagementList(String[] input) {
        for (String employee : input) {
            data.add(new Employee(employee));
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void insert(IEmployee member) {
        data.add(member);
    }

    @Override
    public IEmployee search(int key) {
        for (IEmployee employee : data) {
            if (employee.getKey() == key) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        ArrayList<IEmployee> matchingEmployees = new ArrayList<>();
        for (IEmployee employee : data) {
            if (employee.getName().equals(name) && employee.getFirstName().equals(firstName)) {
                matchingEmployees.add(employee);
            }
        }
        return matchingEmployees.toArray(new IEmployee[0]);
    }

    @Override
    public int size(Department department) {
        int counter = 0;
        for (IEmployee employee : data) {
            if (employee.getDepartment().equals(department)) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public IEmployee[] members(Department department) {
        ArrayList<IEmployee> matchingEmployees = new ArrayList<>();
        for (IEmployee employee : data) {
            if (employee.getDepartment().equals(department)) {
                matchingEmployees.add(employee);
            }
        }
        return matchingEmployees.toArray(new IEmployee[0]);
    }

    @Override
    public IEmployee[] toArray() {
        return data.toArray(new IEmployee[0]);
    }

    @Override
    public int numberOfOperations() {
        return 0;
    }
}

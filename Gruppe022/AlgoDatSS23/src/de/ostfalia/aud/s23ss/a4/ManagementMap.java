package de.ostfalia.aud.s23ss.a4;

import de.ostfalia.aud.s23ss.base.Department;
import de.ostfalia.aud.s23ss.base.Employee;
import de.ostfalia.aud.s23ss.base.IEmployee;
import de.ostfalia.aud.s23ss.base.IManagement;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ManagementMap implements IManagement {
    HashMap<Integer, IEmployee> data = new HashMap<>();

    public ManagementMap(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        Scanner scan = new Scanner(fileReader);
        scan.nextLine();
        while (scan.hasNextLine()) {
            IEmployee newEmployee = new Employee(scan.nextLine());
            data.put(newEmployee.getKey(), newEmployee);
        }
        fileReader.close();
        scan.close();
    }

    public ManagementMap(String[] input) {
        for (String employee : input) {
            IEmployee newEmployee = new Employee(employee);
            data.put(newEmployee.getKey(), newEmployee);
        }
    }

    public ManagementMap() {}

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void insert(IEmployee member) {
        data.put(member.getKey(), member);
    }

    @Override
    public IEmployee search(int key) {
        return data.get(key);
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        ArrayList<IEmployee> matchingEmployees = new ArrayList<>();
        for (IEmployee employee : data.values()) {
            if (employee.getName().equals(name) && employee.getFirstName().equals(firstName)) {
                matchingEmployees.add(employee);
            }
        }
        return matchingEmployees.toArray(new IEmployee[0]);
    }

    @Override
    public int size(Department department) {
        int counter = 0;
        for (IEmployee employee : data.values()) {
            if (employee.getDepartment().equals(department)) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public IEmployee[] members(Department department) {
        ArrayList<IEmployee> matchingEmployees = new ArrayList<>();
        for (IEmployee employee : data.values()) {
            if (employee.getDepartment().equals(department)) {
                matchingEmployees.add(employee);
            }
        }
        return matchingEmployees.toArray(new IEmployee[0]);
    }

    @Override
    public IEmployee[] toArray() {
        return data.values().toArray(new IEmployee[0]);
    }

    @Override
    public int numberOfOperations() {
        return 0;
    }
}

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
    private final HashMap<Integer, IEmployee> keyMap = new HashMap<>();
    private final HashMap<String, ArrayList<IEmployee>> nameMap = new HashMap<>();
    private final HashMap<Department, ArrayList<IEmployee>> departmentMap = new HashMap<>();

    public ManagementMap(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        Scanner scan = new Scanner(fileReader);
        scan.nextLine();
        while (scan.hasNextLine()) {
            insert(new Employee(scan.nextLine()));
        }
        fileReader.close();
        scan.close();
    }

    public ManagementMap(String[] input) {
        for (String employee : input) {
            insert(new Employee(employee));
        }
    }

    public ManagementMap() {}

    @Override
    public int size() {
        return keyMap.size();
    }

    @Override
    public void insert(IEmployee member) {
        keyMap.put(member.getKey(), member);
        ArrayList<IEmployee> nameList = new ArrayList<>();
        if (nameMap.containsKey(member.getFirstName() + member.getName())) {
            nameList = nameMap.get(member.getFirstName() + member.getName());
        }
        nameList.add(member);
        nameMap.put(member.getFirstName() + member.getName(), nameList);
        ArrayList<IEmployee> departmentList = new ArrayList<>();
        if (departmentMap.containsKey(member.getDepartment())) {
            departmentList = departmentMap.get(member.getDepartment());
        }
        departmentList.add(member);
        departmentMap.put(member.getDepartment(), departmentList);
    }

    @Override
    public IEmployee search(int key) {
        return keyMap.get(key);
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        if (nameMap.get(firstName + name) == null) {
            return new IEmployee[0];
        }
        return nameMap.get(firstName + name).toArray(new IEmployee[0]);
    }

    @Override
    public int size(Department department) {
        if (departmentMap.get(department) == null) {
            return 0;
        }
        return departmentMap.get(department).size();
    }

    @Override
    public IEmployee[] members(Department department) {
        if (departmentMap.get(department) == null) {
            return new IEmployee[0];
        }
        return departmentMap.get(department).toArray(new IEmployee[0]);
    }

    @Override
    public IEmployee[] toArray() {
        return keyMap.values().toArray(new IEmployee[0]);
    }

    @Override
    public int numberOfOperations() {
        return 0;
    }
}

package de.ostfalia.aud.s23ss.a2;

import de.ostfalia.aud.s23ss.base.*;
import de.ostfalia.aud.s23ss.comparator.DepartmentComparator;
import de.ostfalia.aud.s23ss.comparator.KeyComparator;
import de.ostfalia.aud.s23ss.comparator.NameComparator;

import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Comparator;

public class Management implements IManagement {
    private IEmployee[] data = new IEmployee[8];
    private int operations;
    private MergeSort merge = new MergeSort(new KeyComparator());

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
        data = merge.sort(data);

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
        data = merge.sort(data);
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
        int l = 0;
        int r = data.length;
        int m;
        while (l <= r) {
            m = l + ((r - l) / 2);
            if (data[m].getKey() == key) {
                return data[m];
            } else if (data[m].getKey() < key) {
                r = m - 1;
            } else {
                l = m + 1;
            }
            operations++;
        }
        return null;
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        operations = 0;
        IEmployee[] matchingEmployees = new IEmployee[0];
        int i = 0;
        int l = 0;
        int r = data.length;
        int m;
        while (l < r) {
            m = l + ((r - l) / 2);
            if (data[m].getName().equals(name)) {
                while (m >= 0 && data[m].getName().equals(name)) {
                    m--;
                }
                m++;
                while (m < data.length && data[m].getName().equals(name)) {
                    if (data[m].getFirstName().equals(firstName)) {
                        if (i == matchingEmployees.length) {
                            matchingEmployees = Arrays.copyOf(matchingEmployees,
                                                    matchingEmployees.length + 1);
                        }
                        matchingEmployees[i] = data[m];
                        i++;
                    }
                    m++;
                }
                return matchingEmployees;
            } else if (data[m].getName().compareTo(name) > 0) {
                r = m - 1;
            } else if (data[m].getName().compareTo(name) < 0) {
                l = m + 1;
            }
            operations++;
        }
        return null;
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
        int l = 0;
        int r = data.length;
        int m;
        while (l < r) {
            m = l + ((r - l) / 2);
            if (data[m].getDepartment() == department) {
                while (m >= 0 && data[m].getDepartment() == department) {
                    m--;
                }
                m++;
                while (m < data.length && data[m].getDepartment() == department) {
                    if (i == matchingEmployees.length) {
                        matchingEmployees = Arrays.copyOf(matchingEmployees,
                                matchingEmployees.length + 1);
                    }
                    matchingEmployees[i] = data[m];
                    i++;
                    m++;
                }
                return matchingEmployees;
            } else if (data[m].getDepartment().toString().compareTo(department.toString()) > 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
            operations++;
        }
        return null;
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

    public static void main(String[] args) throws IOException {
//        Management management = new Management(new String[]{
//                "10049;1961-04-24;Tramer;Basil;F;1992-05-04;Sales",
//                "10050;1958-05-21;Tramer;Basil;M;1990-12-25;Finance",
//                "51;1953-07-28;Caine;Hidefumi;M;1992-10-15;Finance",
//                "10052;1953-07-28;Tramer;Hidefumi;M;1992-10-15;Production",
//                "10053;1953-07-28;Tramer;Hidefumi;M;1992-10-15;Production",
//                "10354;1953-07-28;Dredge;Yinghua;M;1992-10-15;Sales",
//                "10055;1953-07-28;Caine;Yinghua;M;1992-10-15;Sales"
//        });
        Management management = new Management("/Users/Oliver/Documents/Code/Ostfalia/svn/i-aud-ss2023/Gruppe022/AlgoDatSS23/Materialien/10k_employees.csv");
//        IEmployee[] ems = management.search("Yinghua", "Caine");
//        System.out.println(ems.length);
        for (IEmployee e : management.data) {
            System.out.println(e);
        }
    }
}

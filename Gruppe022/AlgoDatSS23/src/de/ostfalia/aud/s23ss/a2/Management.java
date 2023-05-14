package de.ostfalia.aud.s23ss.a2;

import de.ostfalia.aud.s23ss.base.Employee;
import de.ostfalia.aud.s23ss.base.IEmployee;
import de.ostfalia.aud.s23ss.base.IManagement;
import de.ostfalia.aud.s23ss.base.Department;
import de.ostfalia.aud.s23ss.base.MergeSort;
import de.ostfalia.aud.s23ss.comparator.DepartmentComparator;
import de.ostfalia.aud.s23ss.comparator.KeyComparator;
import de.ostfalia.aud.s23ss.comparator.NameComparator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Management implements IManagement {

    protected IEmployee[] employees = new IEmployee[8];
    protected int operations;
    protected MergeSort mergeSort;

    public Management(String fileName) throws IOException {
        operations = 0;
        FileReader fileReader = new FileReader(fileName);
        Scanner scan = new Scanner(fileReader);
        scan.nextLine();
        int i = 0;
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            employees[i] = new Employee(nextLine);
            i++;
            if (i == employees.length) {
                employees = Arrays.copyOf(employees, employees.length * 2);
            }
            operations++;
        }
        fileReader.close();
        scan.close();
        employees = shorterArray();
        this.mergeSort = new MergeSort(new KeyComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
    }

    public Management(String[] data) {
        for (int i = 0; i < data.length; i++) {
            employees[i] = new Employee(data[i]);
            if (i+1 == employees.length) {
                employees = Arrays.copyOf(employees, employees.length * 2);
            }
        }
        employees = shorterArray();
        this.mergeSort = new MergeSort(new KeyComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
    }

    public Management() {
        this.employees = new IEmployee[8];
    }

    @Override
    public int size() {
        return employees.length;
    }

    @Override
    public void insert(IEmployee member) {
        operations = 0;
        for (int i = 0; i < employees.length; i++) {
            if (employees[i] == null) {
                employees[i] = member;
                operations++;
                break;
            }
        }

        IEmployee[] temp = new IEmployee[employees.length+1];
        for (int i = 0; i < employees.length; i++) {
            temp[i] = employees[i];
        }

        temp[temp.length-1] = member;
        employees = temp;
        operations++;
        employees = shorterArray();
    }

    @Override
    public IEmployee search(int key) {
        operations = 0;
        mergeSort = new MergeSort(new KeyComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
        int l = 0;
        int r = employees.length -1;

        while (l <= r) {
            int m = l + (r - l) / 2;

            if (employees[m].getKey() == key) {
                return employees[m];
            } else if (employees[m].getKey() < key) {
                l = m + 1;
            } else {
                r = m - 1;
            }
            operations++;
        }
        return null;
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        operations = 0;
        mergeSort = new MergeSort(new NameComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
        IEmployee[] matchingEmployees = new IEmployee[0];
        int i = 0;
        int l = 0;
        int r = employees.length;
        int m;
        while (l < r) {
            m = l + ((r - l) / 2);
            if (employees[m].getName().equals(name)) {
                while (m >= 0 && employees[m].getName().equals(name)) {
                    m--;
                    operations++;
                }
                m++;
                while (m < employees.length && employees[m].getName().equals(name)) {
                    if (employees[m].getFirstName().equals(firstName)) {
                        if (i == matchingEmployees.length) {
                            matchingEmployees = Arrays.copyOf(matchingEmployees,
                                    matchingEmployees.length + 1);
                        }
                        matchingEmployees[i] = employees[m];
                        i++;
                    }
                    m++;
                    operations++;
                }
                return matchingEmployees;
            } else if (employees[m].getName().compareTo(name) > 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
            operations++;
        }
        return matchingEmployees;
    }

    @Override
    public int size(Department department) {
        operations = 0;
        mergeSort = new MergeSort(new DepartmentComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
        int i = 0;
        int l = 0;
        int r = employees.length;
        int m;
        int counter = 0;
        while (l < r) {
            m = l + ((r - l) / 2);
            if (employees[m].getDepartment() == department) {
                while (m >= 0 && employees[m].getDepartment() == department) {
                    m--;
                    operations++;
                }
                m++;
                while (m < employees.length && employees[m].getDepartment() == department) {
                    counter++;
                    i++;
                    m++;
                    operations++;
                }
                return counter;
            } else if (employees[m].getDepartment().toString().compareTo(department.toString()) > 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
            operations++;
        }
        return 0;
    }

    @Override
    public IEmployee[] members(Department department) {
        operations = 0;
        mergeSort = new MergeSort(new DepartmentComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
        IEmployee[] matchingEmployees = new IEmployee[0];

        if (employees[0].getDepartment() == department) {
            matchingEmployees = new IEmployee[1];
            matchingEmployees[0] = employees[0];
        }

        int i = 0;
        int l = 0;
        int r = employees.length;
        int m;
        while (l < r) {
            m = l + ((r - l) / 2);
            if (employees[m].getDepartment() == department) {
                while (m >= 0 && employees[m].getDepartment() == department) {
                    m--;
                    operations++;
                }
                m++;
                while (m < employees.length && employees[m].getDepartment() == department) {
                    if (i == matchingEmployees.length) {
                        matchingEmployees = Arrays.copyOf(matchingEmployees,
                                matchingEmployees.length + 1);
                    }
                    matchingEmployees[i] = employees[m];
                    i++;
                    m++;
                    operations++;
                }
                mergeSort = new MergeSort(new KeyComparator());
                mergeSort.sort(matchingEmployees, 0, matchingEmployees.length- 1);
                return matchingEmployees;
            } else if (employees[m].getDepartment().toString().compareTo(department.toString()) > 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
            operations++;
        }
        mergeSort = new MergeSort(new KeyComparator());
        mergeSort.sort(matchingEmployees, 0, matchingEmployees.length- 1);
        return matchingEmployees;
    }

    @Override
    public IEmployee[] toArray() {
        employees = shorterArray();
        this.mergeSort = new MergeSort(new KeyComparator());
        employees = mergeSort.sort(employees, operations);
        operations += mergeSort.getOperations();
        return employees;
    }

    @Override
    public int numberOfOperations() {
        return operations;
    }

    public IEmployee[] shorterArray() {
        int counter = 0;
        while (employees[counter] != null) {
            counter++;
            if (counter == employees.length) {
                return employees;
            }
        }
        IEmployee[] shorter = new IEmployee[counter];
        for (int i = 0; i < counter; i++) {
            shorter[i] = employees[i];
        }
        return shorter;
    }

    public static void main(String[] args) throws IOException {
        Management management = new Management();
        Employee em1 = new Employee("10855;1957-08-07;Breannda;Billingsley;F;1991-08-05;Finance");
        Employee em2 = new Employee("10041;1959-08-27;Uri;Lenart;F;1989-11-12;Sales");
        Employee em3 = new Employee("10942;1952-08-08;Toshimitsu;Larfeldt;F;1989-09-08;Development");

        management.insert(em1);
        management.insert(em2);
        management.insert(em3);

        IEmployee[] mem = management.members(Department.DEVELOPMENT);

        for (IEmployee em : mem) {
            System.out.println(em);
        }
    }
}

package de.ostfalia.aud.s23ss.a3;

import de.ostfalia.aud.s23ss.base.Employee;
import de.ostfalia.aud.s23ss.base.IEmployee;
import de.ostfalia.aud.s23ss.base.IManagement;
import de.ostfalia.aud.s23ss.base.Tree;
import de.ostfalia.aud.s23ss.base.Department;
import de.ostfalia.aud.s23ss.base.MergeSort;
import de.ostfalia.aud.s23ss.comparator.DepartmentComparator;
import de.ostfalia.aud.s23ss.comparator.KeyComparator;
import de.ostfalia.aud.s23ss.comparator.NameComparator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Management implements IManagement {
    private Tree tree;
    private String[] data;
    private Comparator<IEmployee> comparator = new KeyComparator();

    public Management(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        Scanner scan = new Scanner(fileReader);
        scan.nextLine();
        this.data = new String[10000];
        int i = 0;
        while (scan.hasNextLine()) {
            data[i] = scan.nextLine();
            i++;
        }
        newTree(comparator);
        fileReader.close();
        scan.close();
    }

    public Management(String[] data) {
        this.data = data;
        newTree(comparator);
    }

    public Management() {
        data = new String[0];
        tree = new Tree();
    }

    private void newTree(Comparator<IEmployee> comparator) {
        tree = new Tree(new Employee(data[0]), comparator);
        for (int i = 1; i < data.length; i++) {
            tree.add(new Employee(data[i]));
        }
    }

    private Tree sortTree(IEmployee[] data, Comparator<IEmployee> comparator) {
        Tree tree = new Tree(data[0], comparator);
        for (int i = 1; i < data.length; i++) {
            tree.add(data[i]);
        }
        return tree;
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public void insert(IEmployee member) {
        addData(member);
        if (tree.getNode() == null) {
            tree = new Tree(member, new KeyComparator());
        } else {
            tree.add(member);
        }
    }

    @Override
    public IEmployee search(int key) {
        if (!(comparator instanceof KeyComparator)) {
            comparator = new KeyComparator();
            newTree(comparator);
        }
        return tree.search(key);
    }

    @Override
    public IEmployee[] search(String name, String firstName) {
        if (!(comparator instanceof NameComparator)) {
            comparator = new NameComparator();
            newTree(comparator);
        }
        Tree result = tree.search(name, firstName);
        if (result == null) {
            return new IEmployee[0];
        }
        IEmployee[] returnArray = new IEmployee[result.size()];
        int i = 0;
        for (IEmployee e : result.toArray(result)) {
            if (e.getFirstName().equals(firstName)) {
                returnArray[i] = e;
                i++;
            } else {
                returnArray = Arrays.copyOf(returnArray, returnArray.length - 1);
            }
        }
        return returnArray;
    }

    @Override
    public int size(Department department) {
        if (!(comparator instanceof DepartmentComparator)) {
            comparator = new DepartmentComparator();
            newTree(comparator);
        }
        Tree searched = tree.search(department);
        if (searched == null) {
            return 0;
        }
        return searched.size();
    }

    @Override
    public IEmployee[] members(Department department) {
        if (!(comparator instanceof DepartmentComparator)) {
            comparator = new DepartmentComparator();
            newTree(comparator);
        }
        Tree result = tree.search(department);
        if (result == null) {
            return new IEmployee[0];
        }
        MergeSort merge = new MergeSort(new KeyComparator());
        return merge.sort(result.toArray(result), 0);
    }

    @Override
    public IEmployee[] toArray() {
        IEmployee[] result = tree.toArray(tree);
        for (IEmployee e : result) {
            if (e == null) {
                result = Arrays.copyOf(result, result.length - 1);
            }
        }
        return result;
    }

    @Override
    public int numberOfOperations() {
        return tree.getOperations();
    }

    @Override
    public int height() {
        if (tree == null) {
            return 0;
        }
        return tree.depth(tree);
    }

    public void addData(IEmployee member) {
        data = Arrays.copyOf(data, data.length + 1);
        data[data.length - 1] = member.toString();
    }

    public static void main(String[] args) throws IOException {
        Management management = new Management(new String[]{
                "19064;1957-10-31;Hironobu;Gecsei;F;1995-02-23;Service",
                "18956;1964-08-02;Muzhong;Zizka;F;1990-01-18;Marketing",
                "10388;1953-03-29;Hironoby;Kaiser;M;1996-03-24;Sales",
                "13740;1960-02-29;Rafols;Taubman;F;1991-04-24;Development",
                "14529;1959-05-25;Murthy;Covnot;M;1995-05-01;Finance",
                "16044;1956-10-09;Mang;Heinisuo;F;1985-07-11;Service",
                "14349;1956-11-06;Taegyun;McFarlin;M;1992-09-09;Development",
                "15742;1963-05-16;Shaibal;Gecsei;M;1991-07-19;Management",
                "19612;1954-10-13;Chenxi;Erdmenger;M;1986-08-25;Service",
                "14262;1958-08-19;Mizuhito;Flexer;F;1985-08-12;Service",
                "15524;1960-09-18;Qiwen;Veldwijk;M;1989-06-12;Manpower",
                "16278;1954-06-11;Marin;Pokrovskii;M;1989-02-20;Production",
                "15149;1964-02-11;Erzsebet;Chandrasekhar;F;1992-12-29;Development",
                "13146;1953-11-01;Haldon;Erie;M;1985-05-25;Sales",
                "19064;1957-10-31;Hironobu;Gecsei;F;1995-02-23;Service"
        });
        Management management10k = new Management("AlgoDatSS23/Materialien/10k_employees.csv");
        Management managementNoArgs = new Management();
        management.insert(management10k.toArray()[1]);
        System.out.println(Arrays.toString(managementNoArgs.toArray()));

        Employee employee = new Employee("10855;1957-08-07;Breannda;Billingsley;F;1991-08-05;Finance");
        management10k.insert(employee);
        System.out.println(Arrays.toString(management10k.search("Eastman", "Domenico")));
    }
}

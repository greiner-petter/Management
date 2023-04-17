package de.ostfalia.aud.s23ss.comparator;

import de.ostfalia.aud.s23ss.base.IEmployee;

import java.util.Comparator;

public class DepartmentComparator implements Comparator<IEmployee> {

    @Override
    public int compare(IEmployee o1, IEmployee o2) {
        return o1.getDepartment().toString().compareTo(o2.getDepartment().toString());
    }
}

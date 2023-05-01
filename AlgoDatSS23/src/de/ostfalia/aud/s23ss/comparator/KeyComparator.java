package de.ostfalia.aud.s23ss.comparator;

import de.ostfalia.aud.s23ss.base.IEmployee;

import java.util.Comparator;

public class KeyComparator implements Comparator<IEmployee> {

    @Override
    public int compare(IEmployee o1, IEmployee o2) {
        return Integer.compare(o1.getKey(), o2.getKey());
    }
}

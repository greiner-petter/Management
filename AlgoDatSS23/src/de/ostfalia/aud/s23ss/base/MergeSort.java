package de.ostfalia.aud.s23ss.base;

import java.util.Comparator;

public class MergeSort {
    Comparator<IEmployee> comparator;
    IEmployee[] data;

    public MergeSort(Comparator<IEmployee> comparator) {
        this.comparator = comparator;
    }

    public IEmployee[] sort(IEmployee[] data) {
        this.data = data;
        mergeSort(data);
        return data;
    }

    private void mergeSort(IEmployee[] arr) {
        if (arr.length > 1) {
            int midpoint = arr.length / 2;
            IEmployee[] l_arr = new IEmployee[midpoint];
            IEmployee[] r_arr = new IEmployee[arr.length - midpoint];
            int L_index = 0;
            int R_index = 0;
            while (L_index < l_arr.length) {
                l_arr[L_index] = arr[L_index];
                if (L_index + 1 < l_arr.length) {
                    l_arr[L_index + 1] = arr[L_index + 1];
                    L_index++;
                }
                L_index++;
            }
            L_index = midpoint;
            while (R_index < r_arr.length) {
                r_arr[R_index] = arr[L_index];
                if (R_index + 1 < r_arr.length) {
                    r_arr[R_index + 1] = arr[L_index + 1];
                    L_index++;
                    R_index++;
                }
                L_index++;
                R_index++;
            }
            mergeSort(l_arr);
            mergeSort(r_arr);
            int l_index = 0;
            int r_index = 0;
            int index = 0;
            while (l_index < l_arr.length && r_index < r_arr.length) {
                if (comparator.compare(l_arr[l_index], r_arr[r_index]) < 0 ) {
                    arr[index] = l_arr[l_index];
                    l_index++;
                } else {
                    arr[index] = r_arr[r_index];
                    r_index++;
                }
                index++;
            }
            while (l_index < l_arr.length) {
                arr[index] = l_arr[l_index];
                l_index++;
                index++;
            }
            while (r_index < r_arr.length) {
                arr[index] = r_arr[r_index];
                r_index++;
                index++;
            }
        }
    }
}

package de.ostfalia.aud.s23ss.base;


import java.util.Comparator;

public class MergeSort {
    Comparator<IEmployee> comparator;
    int operations;

    public MergeSort(Comparator<IEmployee> comparator) {
        this.comparator = comparator;
    }

    public IEmployee[] sort(IEmployee[] data, int operations) {
        this.operations = operations;
        return sort(data, 0, data.length-1);
    }

    public IEmployee[] sort(IEmployee[] data, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            sort(data, left, middle);
            sort(data, middle + 1, right);
            merge(data, left, middle, right);
        }
        return data;
    }

    public void merge(IEmployee[] data, int left, int middle, int right) {
        IEmployee[] arr = new IEmployee[data.length];
        int leftLeft;
        int rightLeft;
        for (leftLeft = left; leftLeft <= middle; leftLeft++) {
            arr[leftLeft] = data[leftLeft];
        }
        for (rightLeft = middle + 1; rightLeft <= right; rightLeft++) {
            arr[right + middle + 1 - rightLeft] = data[rightLeft];
        }
        leftLeft = left;
        rightLeft = right;
        for (int k = left; k <= right; k++) {
            if (comparator.compare(arr[leftLeft], arr[rightLeft]) < 0) {
                data[k] = arr[leftLeft];
                leftLeft++;
            } else {
                data[k] = arr[rightLeft];
                rightLeft--;
            }
        }
    }

    public int getOperations() {
        return operations;
    }
}

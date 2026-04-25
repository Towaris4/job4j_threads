package ru.job4j.pools;

import java.util.concurrent.RecursiveTask;

public class ParallelSearch {

    public static int findIndex(int[] array, int element) {
        return findIndex(array, 0, array.length - 1, element);
    }

    private static int findIndex(int[] array, int from, int to, int element) {
        if ((to - from) < 10) {
            for (int i = from; to >= i; i++) {
                if (array[i] == element) {
                    return i;
                }
            }
        } else {
            int middle = (from + to) / 2;
            int leftResult = findIndex(array, from, middle, element);
            if (leftResult != -1) {
                return leftResult;
            }
            int rightResult = findIndex(array, middle + 1, to, element);
            if (rightResult != -1) {
                return rightResult;
            }
        }
        return -1;
    }

}
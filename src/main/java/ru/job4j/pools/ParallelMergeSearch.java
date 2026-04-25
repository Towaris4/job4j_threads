package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSearch<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final int from;
    private final int to;
    private final T element;

    public ParallelMergeSearch(T[] array, int from, int to, T element) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.element = element;
    }

    @Override
    protected Integer compute() {
        if ((to - from) < 10) {
            for (int i = from; to >= i; i++) {
                if (element.equals(array[i])) {
                    return i;
                }
            }
        } else {
            int middle = (from + to) / 2;
            ParallelMergeSearch leftResult = new ParallelMergeSearch(array, from, middle, element);
            ParallelMergeSearch rightResult = new ParallelMergeSearch(array, middle + 1, to, element);
            leftResult.fork();
            rightResult.fork();
            int left = (int) leftResult.join();
            if (left != -1) {
                return left;
            }
            int right = (int) rightResult.join();
            if (right != -1) {
                return right;
            }
        }
        return -1;
    }

    public static <T> Integer findIndex(T[] array, T element) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return (Integer) forkJoinPool.invoke(new ParallelMergeSearch(array, 0, array.length - 1, element));
    }
}
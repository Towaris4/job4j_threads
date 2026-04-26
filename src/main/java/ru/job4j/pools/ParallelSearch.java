package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final int from;
    private final int to;
    private final T element;

    public ParallelSearch(T[] array, int from, int to, T element) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.element = element;
    }

    @Override
    protected Integer compute() {
        if ((to - from) < 10) {
            return getInteger();
        }
        int middle = (from + to) / 2;
        ParallelSearch leftResult = new ParallelSearch(array, from, middle, element);
        ParallelSearch rightResult = new ParallelSearch(array, middle + 1, to, element);
        leftResult.fork();
        rightResult.fork();
        int left = (int) leftResult.join();
        int right = (int) rightResult.join();
        return Math.max(left, right);
    }

    private Integer getInteger() {
        for (int i = from; to >= i; i++) {
            if (element.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <T> Integer findIndex(T[] array, T element) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return (Integer) forkJoinPool.invoke(new ParallelSearch(array, 0, array.length - 1, element));
    }
}
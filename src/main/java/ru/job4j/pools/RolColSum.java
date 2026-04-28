package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            sums[i] = new Sums();
            for (int j = 0; j < matrix.length; j++) {
                sums[i].setRowSum(sums[i].getRowSum() + matrix[i][j]);
                sums[i].setColSum(sums[i].getColSum() + matrix[j][i]);
            }
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] sums = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            futures.put(i, resolve(i, matrix));
        }
        for (Integer key : futures.keySet()) {
            sums[key] = futures.get(key).get();
        }
        return sums;
    }

    public static CompletableFuture<Sums> resolve(int i, int[][] matrix) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Sums sum = new Sums();
                    for (int j = 0; j < matrix.length; j++) {
                        sum.setRowSum(sum.getRowSum() + matrix[i][j]);
                        sum.setColSum(sum.getColSum() + matrix[j][i]);
                    }
                    return sum;
                }
        );
    }
}
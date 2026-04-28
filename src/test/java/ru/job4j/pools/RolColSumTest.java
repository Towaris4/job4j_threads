package ru.job4j.pools;

import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutionException;
import static org.assertj.core.api.Assertions.assertThat;

class RolColSumTest {

    @Test
    void testSum2x2Matrix() {
        int[][] matrix = {
                {1, 2},
                {3, 4}
        };
        Sums[] expected = {new Sums(3, 4), new Sums(7, 6)};
        Sums[] sums = RolColSum.sum(matrix);
        assertThat(sums).isEqualTo(expected);
    }

    @Test
    void testSum3x3Matrix() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] expected = {
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        Sums[] sums = RolColSum.sum(matrix);
        assertThat(sums).isEqualTo(expected);
    }

    @Test
    void testSumSingleElement() {
        int[][] matrix = {{10}};
        Sums[] expected = {new Sums(10, 10)};
        Sums[] sums = RolColSum.sum(matrix);
        assertThat(sums).isEqualTo(expected);
    }

    @Test
    void testSumEmptyMatrix() {
        int[][] matrix = new int[0][0];
        Sums[] expected = {};
        Sums[] sums = RolColSum.sum(matrix);
        assertThat(sums).isEqualTo(expected);
    }

    @Test
    void testAsyncSum2x2Matrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2},
                {3, 4}
        };
        Sums[] expected = {new Sums(3, 4), new Sums(7, 6)};
        Sums[] sums = RolColSum.asyncSum(matrix);
        assertThat(sums).isEqualTo(expected);
    }

    @Test
    void testAsyncSumEqualsSyncSum() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] sync = RolColSum.sum(matrix);
        Sums[] async = RolColSum.asyncSum(matrix);
        assertThat(async).isEqualTo(sync);
    }

    @Test
    void testAsyncSumWithZerosAndNegatives() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {0, -1},
                {-2, 3}
        };
        Sums[] expected = {new Sums(-1, -2), new Sums(1, 2)};
        Sums[] sums = RolColSum.asyncSum(matrix);
        assertThat(sums).isEqualTo(expected);
    }
}
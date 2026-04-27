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
        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertThat(sums.length).isEqualTo(2);
        // Ожидается: row0 = 1+2=3, col0 = 1+3=4
        assertThat(sums[0].getRowSum()).isEqualTo(3);
        assertThat(sums[0].getColSum()).isEqualTo(4);
        // Ожидается: row1 = 3+4=7, col1 = 2+4=6
        assertThat(sums[1].getRowSum()).isEqualTo(7);
        assertThat(sums[1].getColSum()).isEqualTo(6);
    }

    @Test
    void testSum3x3Matrix() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertThat(sums.length).isEqualTo(3);
        assertThat(sums[0].getRowSum()).isEqualTo(6);
        assertThat(sums[0].getColSum()).isEqualTo(12);
        assertThat(sums[1].getRowSum()).isEqualTo(15);
        assertThat(sums[1].getColSum()).isEqualTo(15);
        assertThat(sums[2].getRowSum()).isEqualTo(24);
        assertThat(sums[2].getColSum()).isEqualTo(18);
    }

    @Test
    void testSumSingleElement() {
        int[][] matrix = {{10}};
        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertThat(sums.length).isEqualTo(1);
        assertThat(sums[0].getRowSum()).isEqualTo(10);
        assertThat(sums[0].getColSum()).isEqualTo(10);
    }

    @Test
    void testSumEmptyMatrix() {
        int[][] matrix = new int[0][0];
        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertThat(sums).isNotNull();
        assertThat(sums.length).isEqualTo(0);
    }

    @Test
    void testAsyncSum2x2Matrix() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2},
                {3, 4}
        };
        RolColSum.Sums[] sums = RolColSum.asyncSum(matrix);

        assertThat(sums.length).isEqualTo(2);
        assertThat(sums[0].getRowSum()).isEqualTo(3);
        assertThat(sums[0].getColSum()).isEqualTo(4);
        assertThat(sums[1].getRowSum()).isEqualTo(7);
        assertThat(sums[1].getColSum()).isEqualTo(6);
    }

    @Test
    void testAsyncSumEqualsSyncSum() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        RolColSum.Sums[] sync = RolColSum.sum(matrix);
        RolColSum.Sums[] async = RolColSum.asyncSum(matrix);

        assertThat(async.length).isEqualTo(sync.length);
        for (int i = 0; i < sync.length; i++) {
            assertThat(async[i].getRowSum())
                    .as("Row sum mismatch at index " + i)
                    .isEqualTo(sync[i].getRowSum());
            assertThat(async[i].getColSum())
                    .as("Col sum mismatch at index " + i)
                    .isEqualTo(sync[i].getColSum());
        }
    }

    @Test
    void testAsyncSumWithZerosAndNegatives() throws ExecutionException, InterruptedException {
        int[][] matrix = {
                {0, -1},
                {-2, 3}
        };
        RolColSum.Sums[] sums = RolColSum.asyncSum(matrix);

        assertThat(sums.length).isEqualTo(2);
        assertThat(sums[0].getRowSum()).isEqualTo(-1); // 0 + (-1)
        assertThat(sums[0].getColSum()).isEqualTo(-2); // 0 + (-2)
        assertThat(sums[1].getRowSum()).isEqualTo(1);  // (-2) + 3
        assertThat(sums[1].getColSum()).isEqualTo(2);  // (-1) + 3
    }
}
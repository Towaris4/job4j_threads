package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParallelSearchTest {
    @Test
    public void whenFindElementSuccesful() {
        int[] array = {0, 1, 2, 44, 28};
        int element = 44;
        int result = ParallelSearch.findIndex(array, element);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void whenFindElementFail() {
        int[] array = {0, 1, 2, 44, 28};
        int element = 88;
        int result = ParallelSearch.findIndex(array, element);
        assertThat(result).isEqualTo(-1);
    }
}
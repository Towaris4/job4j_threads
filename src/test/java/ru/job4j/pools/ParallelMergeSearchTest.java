package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParallelMergeSearchTest {

    @Test
    public void whenFindElementSuccesful() {
        Integer[] array = {0, 1, 2, 44, 28};
        Integer element = 44;
        int result = ParallelMergeSearch.findIndex(array, element);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void whenDifferentTypeElementFail() {
        String[] array = {" ", " ", "okay", " ", " "};
        String element = "okay";
        int result = ParallelMergeSearch.findIndex(array, element);
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void whenLargeArray() {
        String[] array = {" ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", "okay", " "};
        String element = "okay";
        int result = ParallelMergeSearch.findIndex(array, element);
        assertThat(result).isEqualTo(24);
    }

    @Test
    public void whenFindFailLargeArray() {
        String[] array = {" ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ", " ",
                " ", " ", " ", " ", " ", " ", " ", " ", " "};
        String element = "no";
        int result = ParallelMergeSearch.findIndex(array, element);
        assertThat(result).isEqualTo(-1);
    }
}
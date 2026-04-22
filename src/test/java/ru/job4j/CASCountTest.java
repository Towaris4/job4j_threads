package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CASCountTest {

    @Test
    public void whenIncrementThenGet() throws InterruptedException {
        CASCount casCount = new CASCount();
        int result;
        Thread first = new Thread(() -> {
                casCount.increment();
                casCount.increment();
        });
        first.start();
        Thread second = new Thread(() -> {
            casCount.increment();
            casCount.increment();
        });
        second.start();
        first.join();
        second.join();
        result = casCount.get();
        assertThat(result).isEqualTo(4);
    }
}
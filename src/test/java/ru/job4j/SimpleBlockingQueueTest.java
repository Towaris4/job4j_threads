package ru.job4j;

import org.junit.jupiter.api.Test;
import ru.job4j.synch.SingleLockList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    void offerAndPollInTwoThreads() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        List<Integer> result = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                result.add(queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread producer = new Thread(() -> queue.offer(1));
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        assertThat(result).hasSize(1).contains(1);
    }
}
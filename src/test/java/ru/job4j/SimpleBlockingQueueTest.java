package ru.job4j;

import org.junit.jupiter.api.Test;
import ru.job4j.synch.SingleLockList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    void offerAndPollInTwoThreads() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(100);
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

    @Test
    void limitExceeded() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        List<Integer> result = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                result.add(queue.poll());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread producer = new Thread(() -> {
            queue.offer(1);
            queue.offer(2);
        });
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        assertThat(result).hasSize(1).contains(1);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(100);
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(
                            queue::offer
                    );
                }
        );

        Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        consumer.start();
        producer.join();
        while (buffer.size() < 5) {
            Thread.sleep(10);
        }
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }
}
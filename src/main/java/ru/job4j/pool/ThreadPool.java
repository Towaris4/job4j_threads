package ru.job4j.pool;

import ru.job4j.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(100);
    private final int size = Runtime.getRuntime().availableProcessors();
    private volatile boolean run = true;

    public ThreadPool() throws InterruptedException {
            for (int i = 1; i <= size; i++) {
                Thread thread = new Thread(() -> {
                    try {
                        while (run) {
                            tasks.poll().run();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threads.add(thread);
                thread.start();
        }
    }

    public void work(Runnable job) {
        try {
            tasks.offer(job);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        run = false;
        for (Thread thread : threads) {
            try {
                thread.interrupt();
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
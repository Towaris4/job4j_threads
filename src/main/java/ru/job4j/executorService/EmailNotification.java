package ru.job4j.executorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public void send(String subject, String body, String email);

    public void emailTo(User user) {
        String name = user.email();
        String email = user.email();
        String subject = String.format("Notification %s to email %s.", name, email);
        String body = String.format("Add a new event to %s.", name);
        pool.submit(new Runnable() {
            @Override
            public void run() {
                send(subject, body, email);
            }
        });
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

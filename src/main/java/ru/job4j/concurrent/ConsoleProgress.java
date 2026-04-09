package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    public static void main(String[] args) {
        try {
            Thread progress = new Thread(new ConsoleProgress());
            progress.start();
            Thread.sleep(5000);
            progress.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            var process = new char[] {'-', '\\', '|', '/'};
            for (int i = 0; i < 4; i++) {
                System.out.print("\r load: " + process[i]);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("\rLoad complete.");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}

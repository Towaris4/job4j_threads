package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> { });
        Thread second = new Thread(
                () -> { });
        taskMethod(first, "First");
        taskMethod(second, "Second");
    }

    private static void taskMethod(Thread thread, String name) {
        System.out.println(name + ":" + thread.getState());
        thread.start();
        while (thread.getState() != Thread.State.TERMINATED) {
            System.out.println(name + ":" + thread.getState());
        }
        System.out.println(name + ":" + thread.getState());
    }
}
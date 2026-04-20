package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();

    public synchronized void offer(T value) {
        queue.add(value);
        notify();
    }

    public synchronized T poll() throws InterruptedException {
        T result = null;
        while (result == null) {
            result = queue.poll();
            if (result == null) {
                wait();
            }
        }
        return result;
    }
}
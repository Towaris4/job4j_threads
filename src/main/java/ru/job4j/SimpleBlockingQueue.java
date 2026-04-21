package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();

    int size;
    int count;

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized void offer(T value) {
        if (size > count) {
            queue.add(value);
            notifyAll();
            count++;
        }
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
                wait();
            }
        count--;
        return queue.poll();
    }
}
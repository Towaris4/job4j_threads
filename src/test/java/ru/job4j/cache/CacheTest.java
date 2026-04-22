package ru.job4j.cache;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheTest {
    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1,   "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    void whenConcurrentAddSameIdOnlyOneSucceeds() throws InterruptedException {
        var cache = new Cache();
        var results = new CopyOnWriteArrayList<Boolean>();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                try {
                    results.add(cache.add(new Base(1, "ThreadBase", 1)));
                } catch (OptimisticException e) {
                    results.add(false);
                }
            });
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long successCount = results.stream().filter(Boolean::booleanValue).count();
        assertThat(successCount)
                .as("Только один поток должен успешно добавить элемент")
                .isEqualTo(1);
        assertThat(cache.findById(1)).isPresent();
    }

    @Test
    void whenConcurrentUpdateOptimisticLockWorks() throws InterruptedException {
        var cache = new Cache();
        cache.add(new Base(1, "Init", 1));
        var outcomes = new CopyOnWriteArrayList<String>();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                try {
                    cache.update(new Base(1, "Updated", 1));
                    outcomes.add("success");
                } catch (OptimisticException e) {
                    outcomes.add("exception");
                }
            });
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long successCount = outcomes.stream().filter("success"::equals).count();
        long exceptionCount = outcomes.stream().filter("exception"::equals).count();
        assertThat(successCount).isEqualTo(1);
        assertThat(exceptionCount).isEqualTo(4);
        assertThat(cache.findById(1).get().version()).isEqualTo(2);
        assertThat(cache.findById(1).get().name()).isEqualTo("Updated");
    }

    @Test
    void whenProducerConsumerCacheOperations() throws InterruptedException {
        var cache = new Cache();
        var buffer = new CopyOnWriteArrayList<String>();
        var readFlags = new boolean[5];
        var producer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    cache.add(new Base(i, "Prod" + i, 1));
                    Thread.sleep(10);
                }
            } catch (OptimisticException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        var consumer = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    for (int i = 0; i < 5; i++) {
                        if (!readFlags[i]) {
                            var opt = cache.findById(i);
                            if (opt.isPresent()) {
                                buffer.add(opt.get().name());
                                readFlags[i] = true;
                            }
                        }
                    }
                    if (buffer.size() >= 5) {
                        break;
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        consumer.start();
        producer.start();
        producer.join();
        while (buffer.size() < 5) {
            Thread.sleep(10);
        }
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactlyInAnyOrder("Prod0", "Prod1", "Prod2", "Prod3", "Prod4");
    }
}
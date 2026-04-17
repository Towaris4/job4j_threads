package ru.job4j;

public final class Cache {
    private static Cache cache;

    public static Cache getInstance() {
        synchronized (Cache.class) {
            if (cache == null) {
                cache = new Cache();
            }
            return cache;

        }
    }
}
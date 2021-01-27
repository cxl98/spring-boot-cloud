package com.cxl.cloud.common.cache;

import java.util.concurrent.*;

//通用缓存
public class GenericCache<K, V> {
    private final ConcurrentMap<K, Future<V>> cache = new ConcurrentHashMap<>();

    private Future<V> createFutureIfAbsent(final K key, final Callable<V> callable) {
        Future<V> future = cache.get(key);
        if (future == null) {
            final FutureTask<V> futureTask = new FutureTask<>(callable);
            future = cache.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }
        return future;
    }

    public V getValue(final K key, final Callable<V> callable) throws ExecutionException, InterruptedException {
        try {
            final Future<V> future = createFutureIfAbsent(key, callable);
            return future.get();
        } catch (InterruptedException |ExecutionException e) {
            cache.remove(key);
            throw e;
        }
    }
    public void setValueIfAbsent(final K key,final V value){
        createFutureIfAbsent(key, () -> value);
    }
    public void removeValue(final K key){
        cache.remove(key);
    }
}

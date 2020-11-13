package com.geek45.wxback.common;

import com.geek45.wxback.vo.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {

    private Map<String, Map<String, Cache>> cache = new ConcurrentHashMap<>();

    public void remove(){
        this.cache.forEach((s, stringCacheMap) -> stringCacheMap.forEach((s1, cache1) -> {
            if (cache1.getTimeOut() > System.currentTimeMillis()) {
                this.cache.get(s).remove(s1);
            }
        }));
    }

    public String getData(String key, String hashKey) {
        if (!containsKey(key, hashKey)) {
            return null;
        }
        return this.cache.get(key).get(hashKey).getData();
    }

    public boolean containsKey(String key, String hashKey) {
        if (!this.cache.containsKey(key)) {
            return Boolean.FALSE;
        }
        if (!this.cache.get(key).containsKey(hashKey)) {
            return Boolean.FALSE;
        }
        if (this.cache.get(key).get(hashKey).getTimeOut() < System.currentTimeMillis()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void addCache(String key, String hashKey, Cache data) {
        Map<String, Cache> map = this.cache.get(key);
        if (null == map) {
            map = new ConcurrentHashMap<>();
            this.cache.put(key, map);
        }
        map.put(hashKey, data);
    }

    private CacheUtil(){}

    private static Object lock = new Object();
    private static CacheUtil instance = new CacheUtil();

    public static CacheUtil getInstance() {
        synchronized (lock) {
            if (null == instance) {
                instance = new CacheUtil();
            }
            return instance;
        }
    }


}

package com.cw.wizbank.cache;

import java.util.HashMap;

public interface ICacheManager {
    /**
     * Register a cached object, must be a static object.
     * @param key  a name for the cache
     * @param cacheObj cached object
     */
    public abstract void RegisterCache(String key, Object obj);
    
    /**
     * Register a cached object, usually a static object.
     * @param key
     * @param cacheObj
     */
    public abstract void RegisterCache(Object obj);
    
    /**
     * Remove cached object.
     * @param key
     */
    public abstract void unRegisterCache(String key);
    
    /**
     * get a cached object by given name.
     * @param key
     * @return
     * @throws Exception
     */
    public abstract Object getCachedObj(String key);
    
    /**
     * get cached hashmap from cache manager.
     * Note: If the cache doesn't exist, it will create a new cached Hashmap with given key, no need to call RegisterCache() method again.
     * @param autoReg true to call RegisterCache() method to create a cache using given key.
     * @param key
     * @return
     */
    public HashMap getCachedHashmap(String key, boolean autoReg);
    
    /**
     * clear all cached object.
     */
    public abstract void clearCache() ;
    
    /**
     * do initialization here.
     */
    public abstract void init();
}

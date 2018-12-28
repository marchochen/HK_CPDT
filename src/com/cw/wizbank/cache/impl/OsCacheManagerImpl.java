package com.cw.wizbank.cache.impl;

import java.util.HashMap;

import com.cw.wizbank.cache.ICacheManager;
import com.cwn.wizbank.utils.CommonLog;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class OsCacheManagerImpl implements ICacheManager {
    
    private static GeneralCacheAdministrator cacheAdmin = null;
    
    /**
     * Register a cached object, must be a static object.
     * @param cache_name  a name for the cache
     * @param cacheObj cached object
     */
    public void RegisterCache(String key, Object obj) {
        if (obj != null) {
            cacheAdmin.putInCache(key, obj);
            CommonLog.info("new cache added:" + key);
        }
    }
    
    /**
     * Register a cached object, usually a static object.
     * @param cache_name
     * @param cacheObj
     */
    public void RegisterCache(Object obj) {
        if (obj != null) {
            long ts = System.currentTimeMillis();
            String tmp_id = ts + "_" + obj.hashCode();
            cacheAdmin.putInCache(tmp_id, obj);
        }
    }

    /**
     * Remove cached object.
     * @param cache_name
     */
    public void unRegisterCache(String key) {
        cacheAdmin.flushEntry(key);
    }
    
    /**
     * get a cached object by given name.
     * @param cache_name
     * @return
     * @throws Exception
     */
    public Object getCachedObj(String key) {
        try {
            return cacheAdmin.getFromCache(key);
        } catch (NeedsRefreshException e) {
            if (e.getCacheContent() !=null) {
                cacheAdmin.cancelUpdate(key);
            }
            return null;
        }
    }
    
    /**
     * get cached hashmap from cache manager.
     * Note: If the cache doesn't exist, it will create a new cached Hashmap with given key, no need to call RegisterCache() method again.
     * @param autoReg true to call RegisterCache() method to create a cache using given key.
     * @param key
     * @return
     */
    public HashMap getCachedHashmap(String key, boolean autoReg) {
        HashMap map = (HashMap) getCachedObj(key);
        if (map == null && autoReg) {
            map = new HashMap();
            RegisterCache(key, map);
        }
        return map;
    }
    /**
     * clear all cached object.
     */
    public void clearCache() {
        cacheAdmin.flushAll();
    }
    
    
    public void init() {
        cacheAdmin = new GeneralCacheAdministrator();
    }
}

package com.cw.wizbank.cache;

import com.cwn.wizbank.utils.CommonLog;

/**
 * Class to management static object used as a mem cache.
 * @author terry
 *
 */
public class wizbCacheManager {
    
    private static ICacheManager cache_manager;
    private static final String CACHE_MANAGER_CLASS = "com.cw.wizbank.cache.impl.OsCacheManagerImpl";
    public static ICacheManager getInstance() {
        if (cache_manager == null) {
            try {
                cache_manager = (ICacheManager) Class.forName(CACHE_MANAGER_CLASS).newInstance();
                cache_manager.init();
            } catch (InstantiationException e) {
            	CommonLog.error(e.getMessage(),e);
            } catch (IllegalAccessException e) {
            	CommonLog.error(e.getMessage(),e);
            } catch (ClassNotFoundException e) {
            	CommonLog.error(e.getMessage(),e);
            }
        }
        return cache_manager;
    }
    public static void clearCache() {
        if (cache_manager != null) {
            cache_manager.clearCache();
        }
    }
}

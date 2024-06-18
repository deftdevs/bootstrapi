package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.confluence.model.CacheBean;
import com.deftdevs.bootstrapi.confluence.model.CachesBean;

public interface CachesService {

    CachesBean getAllCaches();

    CacheBean getCache(String name);

    void setMaxCacheSize(String name, int newValue);

    void flushCache(String name);

}

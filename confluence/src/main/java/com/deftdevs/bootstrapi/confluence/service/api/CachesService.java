package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.confluence.model.CacheBean;

import java.util.List;

public interface CachesService {

    List<CacheBean> getAllCaches();

    CacheBean getCache(String name);

    void setMaxCacheSize(String name, int newValue);

    void flushCache(String name);

}

package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.confluence.model.CacheModel;

import java.util.List;

public interface CachesService {

    List<CacheModel> getAllCaches();

    CacheModel getCache(String name);

    void setMaxCacheSize(String name, int newValue);

    void flushCache(String name);

}

package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.cache.CacheManager;
import com.atlassian.cache.ManagedCache;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.confluence.model.CacheModel;
import com.deftdevs.bootstrapi.confluence.model.util.CacheModelUtil;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CachesServiceImpl implements CachesService {

    private static final Logger log = LoggerFactory.getLogger(CachesServiceImpl.class);

    private final CacheManager cacheManager;

    public CachesServiceImpl(
            final CacheManager cacheManager) {

        this.cacheManager = cacheManager;
    }

    @Override
    public List<CacheModel> getAllCaches() {
        final Collection<ManagedCache> managedCaches = cacheManager.getManagedCaches();
        final List<CacheModel> cacheModels = new ArrayList<>();
        for (ManagedCache managedCache : managedCaches) {
            try {
                cacheModels.add(CacheModelUtil.toCacheModel(managedCache));
            } catch (Exception e) {
                log.warn("Failed to convert cache '{}': {}", managedCache.getName(), e.getMessage());
            }
        }
        return cacheModels;
    }

    @Override
    public CacheModel getCache(String name) {
        ManagedCache cache = findCache(name);
        return CacheModelUtil.toCacheModel(cache);
    }

    @Override
    public void setMaxCacheSize(String name, int newValue) {
        ManagedCache cache = findCache(name);
        if (!cache.updateMaxEntries(newValue)) {
            throw new BadRequestException(String.format(
                    "Given cache with name '%s' does not support cache resizing", name));
        }
    }

    @Override
    public void flushCache(String name) {
        ManagedCache cache = findCache(name);
        if (!cache.isFlushable()) {
            throw new BadRequestException(String.format(
                    "Given cache with name '%s' is not flushable", name));
        }
        cache.clear();
    }

    private ManagedCache findCache(String name) {
        ManagedCache cache = cacheManager.getManagedCache(name);
        if (cache == null) {
            throw new NotFoundException(String.format(
                    "Given cache with name '%s' not found", name));
        }
        return cache;
    }
}

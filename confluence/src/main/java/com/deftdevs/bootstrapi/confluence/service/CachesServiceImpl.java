package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.cache.CacheManager;
import com.atlassian.cache.ManagedCache;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.confluence.model.CacheModel;
import com.deftdevs.bootstrapi.confluence.model.util.CacheModelUtil;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CachesServiceImpl implements CachesService {

    private final CacheManager cacheManager;

    @Inject
    public CachesServiceImpl(
            @ComponentImport CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public List<CacheModel> getAllCaches() {
        return cacheManager.getManagedCaches().stream()
                .map(CacheModelUtil::toCacheModel)
                .collect(Collectors.toList());
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

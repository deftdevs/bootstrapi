package com.deftdevs.bootstrapi.confluence.service;


import com.atlassian.cache.CacheManager;
import com.atlassian.cache.ManagedCache;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.NotFoundException;
import com.deftdevs.bootstrapi.confluence.model.CacheBean;
import com.deftdevs.bootstrapi.confluence.model.CachesBean;
import com.deftdevs.bootstrapi.confluence.model.util.CacheBeanUtil;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Collectors;

@Component
@ExportAsService(CachesService.class)
public class CachesServiceImpl implements CachesService {

    private final CacheManager cacheManager;

    @Inject
    public CachesServiceImpl(
            @ComponentImport CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public CachesBean getAllCaches() {
        return new CachesBean(cacheManager.getManagedCaches().stream()
                .map(CacheBeanUtil::toCacheBean)
                .collect(Collectors.toList()));
    }

    @Override
    public CacheBean getCache(String name) {
        ManagedCache cache = findCache(name);
        return CacheBeanUtil.toCacheBean(cache);
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

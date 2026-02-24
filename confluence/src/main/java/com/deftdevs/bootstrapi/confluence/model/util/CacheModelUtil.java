package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.cache.CacheStatisticsKey;
import com.atlassian.cache.ManagedCache;
import com.deftdevs.bootstrapi.confluence.model.CacheModel;

import java.util.SortedMap;
import java.util.function.Supplier;

public class CacheModelUtil {

    /**
     * Build CacheModel cache.
     *
     * @return the cache
     */
    public static CacheModel toCacheModel(
            final ManagedCache managedCache) {

        return CacheModel.builder()
            .name(managedCache.getName())
            .currentHeapSizeInByte(getStatistic(managedCache, CacheStatisticsKey.HEAP_SIZE))
            .effectivenessInPercent(getEffectiveness(managedCache))
            .maxObjectCount(managedCache.currentMaxEntries())
            .utilisationInPercent(getUtilization(managedCache))
            .flushable(managedCache.isFlushable())
            .build();
    }

    private static Long getStatistic(
            final ManagedCache cache,
            final CacheStatisticsKey key) {

        final SortedMap<CacheStatisticsKey, Supplier<Long>> statistics = cache.getStatistics();
        final Supplier<Long> supplier = statistics.get(key);
        return supplier != null ? supplier.get() : null;
    }

    private static Double getEffectiveness(
            final ManagedCache cache) {

        final Long hit = getStatistic(cache, CacheStatisticsKey.HIT_COUNT);
        final Long miss = getStatistic(cache, CacheStatisticsKey.MISS_COUNT);
        if (hit == null || miss == null || hit + miss == 0) {
            return null;
        }
        return (double) hit * 100 / (hit + miss);
    }

    private static Double getUtilization(
            final ManagedCache cache) {

        final Long objects = getStatistic(cache, CacheStatisticsKey.SIZE);
        final Integer size = cache.currentMaxEntries();

        if (objects == null || size == null) {
            return null;
        }
        return (double) objects * 100 / size;
    }

    private CacheModelUtil() {
    }
}

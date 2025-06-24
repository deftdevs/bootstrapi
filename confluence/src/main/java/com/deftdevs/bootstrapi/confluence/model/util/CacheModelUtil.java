package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.cache.CacheStatisticsKey;
import com.atlassian.cache.ManagedCache;
import com.deftdevs.bootstrapi.confluence.model.CacheModel;

public class CacheModelUtil {

    /**
     * Build CacheModel cache.
     *
     * @return the cache
     */
    public static CacheModel toCacheModel(
            final ManagedCache managedCache) {

        CacheModel cacheModel = new CacheModel();
        cacheModel.setName(managedCache.getName());
        cacheModel.setCurrentHeapSizeInByte(managedCache.getStatistics().get(CacheStatisticsKey.HEAP_SIZE).get());
        cacheModel.setEffectivenessInPercent(getEffectiveness(managedCache));
        cacheModel.setMaxObjectCount(managedCache.currentMaxEntries());
        cacheModel.setUtilisationInPercent(getUtilization(managedCache));
        cacheModel.setFlushable(managedCache.isFlushable());

        return cacheModel;
    }

    private static double getEffectiveness(
            final ManagedCache cache) {

        long hit = cache.getStatistics().get(CacheStatisticsKey.HIT_COUNT).get();
        long miss = cache.getStatistics().get(CacheStatisticsKey.MISS_COUNT).get();
        return (double) hit * 100 / (hit + miss);
    }


    private static Double getUtilization(
            final ManagedCache cache) {

        // currentMaxEntries can be null so check this first

        long objects = cache.getStatistics().get(CacheStatisticsKey.SIZE).get();
        Integer size = cache.currentMaxEntries();

        if (size != null) {
            return (double) objects * 100 / size;
        }
        return null;
    }

    private CacheModelUtil() {
    }
}

package de.aservo.confapi.confluence.service;

import com.atlassian.cache.CacheManager;
import com.atlassian.cache.CacheStatisticsKey;
import com.atlassian.cache.ManagedCache;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.confluence.model.CacheBean;
import de.aservo.confapi.confluence.model.util.CacheBeanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachesServiceTest {

    @Mock
    private CacheManager cacheManager;

    private CachesServiceImpl cachesService;

    @BeforeEach
    public void setup() {
        cachesService = new CachesServiceImpl(cacheManager);
    }

    @Test
    void testGetAllCaches() {

        ManagedCache cache = mock(ManagedCache.class);
        doReturn("test_cache").when(cache).getName();
        doReturn(null).when(cache).currentMaxEntries();
        doReturn(false).when(cache).isFlushable();
        doReturn(createStatistics(555L, 2L, 1L, 1000L)).when(cache).getStatistics();

        Collection<ManagedCache> cacheCollection = new ArrayList<>();
        cacheCollection.add(cache);

        doReturn(cacheCollection).when(cacheManager).getManagedCaches();

        assertEquals(CacheBeanUtil.toCacheBean(cache), cachesService.getAllCaches().getCaches().iterator().next());

    }

    @Test
    void testGetCache() {

        ManagedCache cache = mock(ManagedCache.class);
        doReturn("test_cache").when(cache).getName();
        doReturn(2000).when(cache).currentMaxEntries();
        doReturn(false).when(cache).isFlushable();
        doReturn(createStatistics(555L, 2L, 1L, 1000L)).when(cache).getStatistics();

        doReturn(cache).when(cacheManager).getManagedCache("test_cache");

        CacheBean cacheBean = cachesService.getCache("test_cache");

        assertEquals(cacheBean, CacheBeanUtil.toCacheBean(cache));

    }

    @Test
    void testGetCacheNotExisting() {
        assertThrows(NotFoundException.class, () -> {
            cachesService.getCache("not_existing_test_cache");
        });
    }

    @Test
    void testSetMaxCacheSizeNotSupported() {
        ManagedCache cache = mock(ManagedCache.class);
        int newSize = 500;
        doReturn(cache).when(cacheManager).getManagedCache("test_cache");
        doReturn(false).when(cache).updateMaxEntries(newSize);

        assertThrows(BadRequestException.class, () -> {
            cachesService.setMaxCacheSize("test_cache", newSize);
        });
    }

    @Test
    void testSetMaxCacheSize() {
        ManagedCache cache = mock(ManagedCache.class);

        int newSize = 500;
        doReturn(cache).when(cacheManager).getManagedCache("test_cache");
        doReturn(true).when(cache).updateMaxEntries(newSize);

        cachesService.setMaxCacheSize("test_cache", newSize);
        verify(cache).updateMaxEntries(newSize);

    }

    @Test
    void testFlushCache() {
        ManagedCache cache = mock(ManagedCache.class);
        doReturn(true).when(cache).isFlushable();
        doReturn(cache).when(cacheManager).getManagedCache("test_cache");

        cachesService.flushCache("test_cache");
        verify(cache).clear();

    }

    @Test
    void testFlushCacheNotFlushable() {
        ManagedCache cache = mock(ManagedCache.class);
        doReturn(false).when(cache).isFlushable();
        doReturn(cache).when(cacheManager).getManagedCache("test_cache");

        assertThrows(BadRequestException.class, () -> {
            cachesService.flushCache("test_cache");
        });
    }

    private SortedMap<CacheStatisticsKey, Supplier<Long>> createStatistics(long heapSize, long missCount, long hitCount, long size) {
        Supplier<Long> supHeapSize = () -> heapSize;
        Supplier<Long> supMissCount = () -> missCount;
        Supplier<Long> supHitCount = () -> hitCount;
        Supplier<Long> supSize = () -> size;

        SortedMap<CacheStatisticsKey, Supplier<Long>> statistics = new TreeMap<>();
        statistics.put(CacheStatisticsKey.HEAP_SIZE, supHeapSize);
        statistics.put(CacheStatisticsKey.MISS_COUNT, supMissCount);
        statistics.put(CacheStatisticsKey.HIT_COUNT, supHitCount);
        statistics.put(CacheStatisticsKey.SIZE, supSize);

        return statistics;
    }

}

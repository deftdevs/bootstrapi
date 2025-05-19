package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.CacheModel;
import com.deftdevs.bootstrapi.confluence.rest.api.CachesResource;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.CACHES)
@SystemAdminOnly
public class CachesResourceImpl implements CachesResource {

    private final CachesService cachesService;

    public CachesResourceImpl(
            final CachesService cachesService) {

        this.cachesService = cachesService;
    }

    @Override
    public Response getCaches() {
        return Response.ok(cachesService.getAllCaches()).build();
    }

    @Override
    public Response getCache(String name) {
        return Response.ok(cachesService.getCache(name)).build();
    }

    @Override
    public Response flushCache(String name) {
        cachesService.flushCache(name);
        return Response.ok(cachesService.getCache(name)).build();
    }

    @Override
    public Response updateCache(String name, CacheModel cache) {

        cachesService.setMaxCacheSize(name, cache.getMaxObjectCount());
        return Response.ok(cachesService.getCache(name)).build();
    }

}

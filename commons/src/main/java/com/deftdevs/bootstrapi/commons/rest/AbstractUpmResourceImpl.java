package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.rest.api.UpmResource;
import com.deftdevs.bootstrapi.commons.service.api.UpmService;

import jakarta.ws.rs.core.Response;
import java.util.Map;

public abstract class AbstractUpmResourceImpl implements UpmResource {

    private final UpmService upmService;

    public AbstractUpmResourceImpl(
            final UpmService upmService) {

        this.upmService = upmService;
    }

    @Override
    public Response getPlugins() {
        final Map<String, PluginModel> pluginModels = upmService.getPlugins();
        return Response.ok(pluginModels).build();
    }

    @Override
    public Response setUpm(
            final UpmModel upmModel) {

        if (upmModel == null) {
            throw new BadRequestException("A UPM configuration must be provided in the request body");
        }

        final ServiceResult<UpmModel> serviceResult = upmService.setUpm(upmModel);
        final UpmModel result = serviceResult.getModel();
        result.setStatus(serviceResult.getStatus());
        final int overallStatus = _AbstractAllResourceImpl.computeOverallStatus(serviceResult.getStatus());
        return Response.status(overallStatus).entity(result).build();
    }
}

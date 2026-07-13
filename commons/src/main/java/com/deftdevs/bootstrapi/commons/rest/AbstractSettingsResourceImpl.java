package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractSettingsModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.rest.api.SettingsResource;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;

import javax.ws.rs.core.Response;

public abstract class AbstractSettingsResourceImpl<S extends AbstractSettingsModel>
        implements SettingsResource<S> {

    private final SettingsService<S> settingsService;

    public AbstractSettingsResourceImpl(
            final SettingsService<S> settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getSettings() {
        final S settingsModel = settingsService.getSettings();
        return Response.ok(settingsModel).build();
    }

    @Override
    public Response setSettings(
            final S settingsModel) {

        if (settingsModel == null) {
            throw new BadRequestException("A settings configuration must be provided in the request body");
        }

        final ServiceResult<S> serviceResult = settingsService.setSettings(settingsModel);
        final S result = serviceResult.getModel();
        result.setStatus(serviceResult.getStatus());
        final int overallStatus = _AbstractAllResourceImpl.computeOverallStatus(serviceResult.getStatus());
        return Response.status(overallStatus).entity(result).build();
    }
}

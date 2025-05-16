package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.rest.api.SettingsResource;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;

import javax.ws.rs.core.Response;

public abstract class AbstractSettingsResourceImpl<B extends SettingsModel, S extends SettingsService<B>>
        implements SettingsResource<B> {

    private final S settingsService;

    public AbstractSettingsResourceImpl(
            final S settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getSettings() {
        final B settingsModel = settingsService.getSettingsGeneral();
        return Response.ok(settingsModel).build();
    }

    @Override
    public Response setSettings(B settingsModel) {
        final B updatedSettingsModel = settingsService.setSettingsGeneral(settingsModel);
        return Response.ok(updatedSettingsModel).build();
    }
}

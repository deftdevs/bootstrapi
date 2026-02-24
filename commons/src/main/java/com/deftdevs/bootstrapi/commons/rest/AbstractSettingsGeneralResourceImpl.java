package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.rest.api.SettingsGeneralResource;
import com.deftdevs.bootstrapi.commons.service.api.SettingsGeneralService;

import javax.ws.rs.core.Response;

public abstract class AbstractSettingsGeneralResourceImpl<B extends SettingsGeneralModel, S extends SettingsGeneralService<B>>
        implements SettingsGeneralResource<B> {

    private final S settingsService;

    public AbstractSettingsGeneralResourceImpl(
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
        final B updatedSettingsGeneralModel = settingsService.setSettingsGeneral(settingsModel);
        return Response.ok(updatedSettingsGeneralModel).build();
    }
}

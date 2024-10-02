package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.rest.api.SettingsResource;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;

import javax.ws.rs.core.Response;

public abstract class AbstractSettingsResourceImpl<B extends SettingsBean, S extends SettingsService<B>>
        implements SettingsResource<B> {

    private final S settingsService;

    public AbstractSettingsResourceImpl(
            final S settingsService) {

        this.settingsService = settingsService;
    }

    @Override
    public Response getSettings() {
        final B settingsBean = settingsService.getSettingsGeneral();
        return Response.ok(settingsBean).build();
    }

    @Override
    public Response setSettings(B settingsBean) {
        final B updatedSettingsBean = settingsService.setSettingsGeneral(settingsBean);
        return Response.ok(updatedSettingsBean).build();
    }
}

package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.rest.api.SettingsSecurityResource;
import com.deftdevs.bootstrapi.commons.service.api.SettingsSecurityService;

import javax.ws.rs.core.Response;

public abstract class AbstractSettingsSecurityResourceImpl<B extends SettingsSecurityModel, S extends SettingsSecurityService<B>>
        implements SettingsSecurityResource<B> {

    private final S settingsSecurityService;

    public AbstractSettingsSecurityResourceImpl(
            final S settingsSecurityService) {

        this.settingsSecurityService = settingsSecurityService;
    }

    @Override
    public Response getSettingsSecurity() {
        final B result = settingsSecurityService.getSettingsSecurity();
        return Response.ok(result).build();
    }

    @Override
    public Response setSettingsSecurity(
            final B bean) {

        final B result = settingsSecurityService.setSettingsSecurity(bean);
        return Response.ok(result).build();
    }
}

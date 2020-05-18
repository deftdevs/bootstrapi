package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.SettingsBean;
import de.aservo.atlassian.confapi.rest.api.SettingsResource;
import de.aservo.atlassian.confapi.service.api.SettingsService;

import javax.ws.rs.core.Response;

public abstract class AbstractSettingsResourceImpl implements SettingsResource {

    private final SettingsService settingsService;

    public AbstractSettingsResourceImpl(final SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public Response getSettings() {
        final SettingsBean settingsBean = settingsService.getSettings();
        return Response.ok(settingsBean).build();
    }

    @Override
    public Response setSettings(SettingsBean settingsBean) {
        final SettingsBean updatedSettingsBean = settingsService.setSettings(settingsBean);
        return Response.ok(updatedSettingsBean).build();
    }
}

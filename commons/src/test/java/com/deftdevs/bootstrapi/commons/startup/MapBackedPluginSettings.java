package com.deftdevs.bootstrapi.commons.startup;

import com.atlassian.sal.api.pluginsettings.PluginSettings;

import java.util.Map;

class MapBackedPluginSettings implements PluginSettings {

    private final Map<String, Object> settings;

    MapBackedPluginSettings(
            final Map<String, Object> settings) {

        this.settings = settings;
    }

    @Override
    public Object get(
            final String key) {

        return settings.get(key);
    }

    @Override
    public Object put(
            final String key,
            final Object value) {

        return settings.put(key, value);
    }

    @Override
    public Object remove(
            final String key) {

        return settings.remove(key);
    }
}

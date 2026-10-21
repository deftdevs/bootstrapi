package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;

import java.util.Map;

public interface UpmService {

    /**
     * Gets all installed plugins with their version and enabled state,
     * keyed by plugin key.
     *
     * @return the installed plugins
     */
    Map<String, PluginModel> getPlugins();

    /**
     * Applies a UPM configuration: resolves, installs and enables (or
     * disables) every plugin of the {@code plugins} map. Plugins already
     * installed in the requested version are not installed again, so
     * re-applying the same configuration is safe.
     *
     * @param upmModel the UPM configuration
     * @return the applied plugins and a per-plugin-key status map; the
     *         resolvers (and their credentials) are not echoed
     */
    ServiceResult<UpmModel> setUpm(
            UpmModel upmModel);

}

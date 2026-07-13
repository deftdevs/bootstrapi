package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;

public interface _AllService<_AllModel extends _AllModelAccessor> {

    /**
     * Apply a complete configuration.
     *
     * @param allModel the configuration to apply
     * @return the updated configuration with status
     */
    _AllModel setAll(
            _AllModel allModel);

}

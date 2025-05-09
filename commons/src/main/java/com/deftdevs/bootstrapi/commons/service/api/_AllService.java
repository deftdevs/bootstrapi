package com.deftdevs.bootstrapi.commons.service.api;

public interface _AllService<_AllBean> {

    /**
     * Apply a complete configuration.
     *
     * @param allBean the configuration to apply
     * @return the updated configuration with status
     */
    _AllBean setAll(
            _AllBean allBean);

}

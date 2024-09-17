package com.deftdevs.bootstrapi.crowd.event;

import com.atlassian.config.util.BootstrapUtils;
import com.atlassian.sal.api.lifecycle.LifecycleAware;

public class PluginLifecycleAware implements LifecycleAware {

    @Override
    public void onStart() {

        BootstrapUtils.getBootstrapManager().isBootstrapped()

    }

    @Override
    public void onStop() {

    }
}

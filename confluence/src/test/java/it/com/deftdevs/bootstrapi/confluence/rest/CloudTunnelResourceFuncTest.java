package it.com.deftdevs.bootstrapi.confluence.rest;

import it.com.deftdevs.bootstrapi.commons.rest.AbstractCloudTunnelResourceFuncTest;

public class CloudTunnelResourceFuncTest extends AbstractCloudTunnelResourceFuncTest {

    // bundled with Confluence
    @Override
    protected boolean isTunnelPluginInstalled() {
        return true;
    }

}

package it.com.deftdevs.bootstrapi.jira.rest;

import it.com.deftdevs.bootstrapi.commons.rest.AbstractCloudTunnelResourceFuncTest;

public class CloudTunnelResourceFuncTest extends AbstractCloudTunnelResourceFuncTest {

    // not bundled with Jira
    @Override
    protected boolean isTunnelPluginInstalled() {
        return false;
    }

}

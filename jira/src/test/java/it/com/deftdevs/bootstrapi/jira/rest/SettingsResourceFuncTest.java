package it.com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import it.com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceFuncTest;

public class SettingsResourceFuncTest extends AbstractSettingsResourceFuncTest {

    @Override
    protected SettingsBean getExampleBean() {
        return SettingsBean.EXAMPLE_1;
    }

}

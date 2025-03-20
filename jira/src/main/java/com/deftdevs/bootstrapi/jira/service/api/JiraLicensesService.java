package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import java.util.Collection;

public interface JiraLicensesService extends LicensesService {

    Collection<LicenseBean> setLicenses(
            Collection<LicenseBean> licenseBeans);

}

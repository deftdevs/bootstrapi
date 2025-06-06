package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;

import java.io.InputStream;

public interface CrowdSettingsBrandingService {

    SettingsBrandingLoginPageModel getLoginPage();

    SettingsBrandingLoginPageModel setLoginPage(
            SettingsBrandingLoginPageModel settingsBrandingLoginPageModel);

    void setLogo(InputStream inputStream);

}

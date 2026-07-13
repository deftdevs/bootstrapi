package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;

import java.io.InputStream;

public interface CrowdSettingsBrandingService {

    SettingsBrandingLoginPageModel getSettingsBrandingLoginPage();

    SettingsBrandingLoginPageModel setSettingsBrandingLoginPage(
            SettingsBrandingLoginPageModel settingsBrandingLoginPageModel);

    void setSettingsBrandingLogo(InputStream inputStream);

}

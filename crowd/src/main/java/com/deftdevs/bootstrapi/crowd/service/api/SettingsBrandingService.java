package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageBean;

import java.io.InputStream;

public interface SettingsBrandingService {

    SettingsBrandingLoginPageBean getLoginPage();

    SettingsBrandingLoginPageBean setLoginPage(
            SettingsBrandingLoginPageBean settingsBrandingLoginPageBean);

    void setLogo(InputStream inputStream);

}

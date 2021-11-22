package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.crowd.model.SettingsBrandingLoginPageBean;

import java.io.InputStream;

public interface SettingsBrandingService {

    SettingsBrandingLoginPageBean getLoginPage();

    SettingsBrandingLoginPageBean setLoginPage(
            SettingsBrandingLoginPageBean settingsBrandingLoginPageBean);

    void setLogo(InputStream inputStream);

}

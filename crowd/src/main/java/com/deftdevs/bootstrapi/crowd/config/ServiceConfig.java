package com.deftdevs.bootstrapi.crowd.config;

import com.deftdevs.bootstrapi.commons.service.api.*;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
import com.deftdevs.bootstrapi.crowd.service.*;
import com.deftdevs.bootstrapi.crowd.service.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Autowired
    private AtlassianConfig atlassianConfig;

    @Autowired
    private HelperConfig helperConfig;

    @Bean
    public _AllService<_AllModel> _allService() {
        return new _AllServiceImpl(
                crowdSettingsGeneralService(),
                directoriesService(),
                applicationsService());
    }

    @Bean
    public ApplicationLinksService applicationLinksService() {
        return new ApplicationLinksServiceImpl(
                atlassianConfig.mutatingApplicationLinkService(),
                atlassianConfig.applinkStatusService(),
                atlassianConfig.typeAccessor(),
                helperConfig.applicationLinksAuthConfigHelper());
    }

    @Bean
    public ApplicationsService applicationsService() {
        return new ApplicationsServiceImpl(
                atlassianConfig.applicationManager(),
                atlassianConfig.defaultGroupMembershipService(),
                atlassianConfig.directoryManager());
    }

    @Bean
    public CrowdSettingsBrandingService settingsBrandingService() {
        return new SettingsBrandingServiceImpl(
                atlassianConfig.propertyManager());
    }

    @Bean
    public CrowdSettingsGeneralService crowdSettingsGeneralService() {
        return new SettingsServiceImpl(
                atlassianConfig.propertyManager());
    }

    @Bean
    public DirectoriesService directoriesService() {
        return new DirectoriesServiceImpl(
                atlassianConfig.directoryManager(),
                groupsService(),
                usersService());
    }

    @Bean
    public GroupsService groupsService() {
        return new GroupsServiceImpl(
                atlassianConfig.directoryManager());
    }

    @Bean
    public LicensesService licensesService() {
        return new LicensesServiceImpl(
                atlassianConfig.atlassianBootstrapManager());
    }

    @Bean
    public MailServerService mailServerService() {
        return new MailServerServiceImpl(
                atlassianConfig.mailConfigurationService());
    }

    @Bean
    public MailTemplatesService mailTemplatesService() {
        return new MailTemplatesServiceImpl(
                atlassianConfig.propertyManager());
    }

    @Bean
    public SessionConfigService sessionConfigService() {
        return new SessionConfigServiceImpl(
                atlassianConfig.propertyManager());
    }

    @Bean
    public TrustedProxiesService trustedProxiesService() {
        return new TrustedProxiesServiceImpl(
                atlassianConfig.propertyManager());
    }

    @Bean
    public UsersService usersService() {
        return new UsersServiceImpl(
                atlassianConfig.crowdService(),
                atlassianConfig.directoryManager(),
                groupsService());
    }

}

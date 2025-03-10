package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.applinks.api.application.bamboo.BambooApplicationType;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType;
import com.atlassian.applinks.api.application.crowd.CrowdApplicationType;
import com.atlassian.applinks.api.application.fecru.FishEyeCrucibleApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.applinks.core.ApplinkStatus;
import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.internal.common.exception.NoAccessException;
import com.atlassian.applinks.internal.common.exception.NoSuchApplinkException;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationException;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.applinks.spi.link.MutableApplicationLink;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.manifest.ManifestNotFoundException;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkType;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.crowd.model.DefaultAuthenticationScenario;
import com.deftdevs.bootstrapi.crowd.model.util.ApplicationLinkBeanUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.CONNECTION_REFUSED;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkStatus.*;
import static com.deftdevs.bootstrapi.crowd.model.util.ApplicationLinkBeanUtil.toApplicationLinkBean;
import static com.deftdevs.bootstrapi.crowd.model.util.ApplicationLinkBeanUtil.toApplicationLinkDetails;

@Named
@ExportAsService(ApplicationLinksService.class)
public class ApplicationLinksServiceImpl implements ApplicationLinksService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLinksServiceImpl.class);

    private final MutatingApplicationLinkService mutatingApplicationLinkService;
    private final TypeAccessor typeAccessor;
    private final ApplinkStatusService applinkStatusService;

    @Inject
    public ApplicationLinksServiceImpl(
            @ComponentImport MutatingApplicationLinkService mutatingApplicationLinkService,
            @ComponentImport TypeAccessor typeAccessor,
            @ComponentImport ApplinkStatusService applinkStatusService) {

        this.mutatingApplicationLinkService = mutatingApplicationLinkService;
        this.typeAccessor = typeAccessor;
        this.applinkStatusService = applinkStatusService;
    }

    @Override
    public List<ApplicationLinkBean> getApplicationLinks() {
        final Iterable<ApplicationLink> applicationLinksIterable = mutatingApplicationLinkService.getApplicationLinks();

        return StreamSupport.stream(applicationLinksIterable.spliterator(),false)
                .map(this::getApplicationLinkBeanWithStatus)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationLinkBean getApplicationLink(
            final UUID uuid) {

        try {
            //finder (new function find_app_link(uuid)) NotFoundException("applink with id cannot")
            MutableApplicationLink applicationLink = (MutableApplicationLink) findApplicationLink(uuid);
            return getApplicationLinkBeanWithStatus(applicationLink);
        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationLinkBean> setApplicationLinks(
            @NotNull final List<ApplicationLinkBean> applicationLinkBeans,
            final boolean ignoreSetupErrors) {

        final Map<URI, ApplicationLinkBean> applicationLinksByRpcUrl = getApplicationLinks().stream()
                .collect(Collectors.toMap(ApplicationLinkBean::getRpcUrl, Function.identity()));

        for (ApplicationLinkBean applicationLink : applicationLinkBeans) {
            if (applicationLinksByRpcUrl.containsKey(applicationLink.getRpcUrl())) {
                setApplicationLink(applicationLink.getUuid(), applicationLink, ignoreSetupErrors);
            } else {
                addApplicationLink(applicationLink, ignoreSetupErrors);
            }
        }

        return getApplicationLinks();
    }

    @Override
    public ApplicationLinkBean setApplicationLink(
            @NotNull final UUID uuid,
            @NotNull final ApplicationLinkBean applicationLinkBean,
            final boolean ignoreSetupErrors) {

        ApplicationId id = new ApplicationId(uuid.toString());
        try {
            //entity must be removed first (there is no update service method)
            ApplicationLink applicationLink = findApplicationLink(uuid);
            mutatingApplicationLinkService.deleteApplicationLink(applicationLink);

            //finally a new entity is added with the known existing server id
            ApplicationLinkDetails linkDetails = ApplicationLinkBeanUtil.toApplicationLinkDetails(applicationLinkBean);
            ApplicationType applicationType = buildApplicationType(applicationLinkBean.getType());
            MutableApplicationLink mutableApplicationLink = mutatingApplicationLinkService.addApplicationLink(id, applicationType, linkDetails);
            return getApplicationLinkBeanWithStatus(mutableApplicationLink);

        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public ApplicationLinkBean addApplicationLink(
            @NotNull final ApplicationLinkBean applicationLinkBean,
            final boolean ignoreSetupErrors) {

        ApplicationLinkDetails linkDetails = toApplicationLinkDetails(applicationLinkBean);

        ApplicationType applicationType = buildApplicationType(applicationLinkBean.getType());

        //check if there is already an application link of supplied type and if yes, remove it
        Class<? extends ApplicationType> appType = applicationType != null ? applicationType.getClass() : null;
        ApplicationLink primaryApplicationLink = mutatingApplicationLinkService.getPrimaryApplicationLink(appType);
        if (primaryApplicationLink != null) {
            log.info("An existing application link configuration '{}' was found and is removed now before adding the new configuration",
                    primaryApplicationLink.getName());
            mutatingApplicationLinkService.deleteApplicationLink(primaryApplicationLink);
        }

        //add new application link, this should always work - even if remote app is not accessible
        ApplicationLink applicationLink;
        try {
            applicationLink = mutatingApplicationLinkService.createApplicationLink(applicationType, linkDetails);
        } catch (ManifestNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

        //configure authenticator, this might fail if setup is incorrect or remote app is unavailable
        try {
            mutatingApplicationLinkService.configureAuthenticationForApplicationLink(applicationLink,
                    new DefaultAuthenticationScenario(), applicationLinkBean.getUsername(), applicationLinkBean.getPassword());
        } catch (AuthenticationConfigurationException e) {
            if (!ignoreSetupErrors) {
                throw new BadRequestException(e.getMessage());
            }
        }

        return getApplicationLinkBeanWithStatus(applicationLink);
    }

    @Override
    public void deleteApplicationLinks(
            final boolean force) {
        if (!force) {
            throw new BadRequestException("'force = true' must be supplied to delete all entries");
        } else {
            for (ApplicationLink applicationLink : mutatingApplicationLinkService.getApplicationLinks()) {
                mutatingApplicationLinkService.deleteApplicationLink(applicationLink);
            }
        }
    }

    @Override
    public void deleteApplicationLink(
            @NotNull final UUID uuid) {

        try {
            MutableApplicationLink applicationLink = (MutableApplicationLink) findApplicationLink(uuid);
            mutatingApplicationLinkService.deleteApplicationLink(applicationLink);
        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private ApplicationType buildApplicationType(ApplicationLinkType linkType) {
        switch (linkType) {
            case BAMBOO:
                return typeAccessor.getApplicationType(BambooApplicationType.class);
            case JIRA:
                return typeAccessor.getApplicationType(JiraApplicationType.class);
            case BITBUCKET:
                return typeAccessor.getApplicationType(BitbucketApplicationType.class);
            case CONFLUENCE:
                return typeAccessor.getApplicationType(ConfluenceApplicationType.class);
            case FISHEYE:
                return typeAccessor.getApplicationType(FishEyeCrucibleApplicationType.class);
            case CROWD:
                return typeAccessor.getApplicationType(CrowdApplicationType.class);
            default:
                throw new NotImplementedException("application type '" + linkType + "' not implemented");
        }
    }

    private ApplicationLink findApplicationLink(UUID uuid) throws TypeNotInstalledException {
        ApplicationLink applicationLink = mutatingApplicationLinkService.getApplicationLink(new ApplicationId(uuid.toString()));
        if(applicationLink == null) {
            throw new NotFoundException("ApplicationLink with id " + uuid.toString() + " not found.");
        }
        return applicationLink;
    }

    private ApplicationLinkBean getApplicationLinkBeanWithStatus(ApplicationLink applicationLink) {

        ApplicationLinkBean applicationLinkBean = toApplicationLinkBean(applicationLink);

        try {
            ApplinkStatus applinkStatus = applinkStatusService.getApplinkStatus(applicationLink.getId());
            if (applinkStatus.isWorking()) {
                applicationLinkBean.setStatus(AVAILABLE);
            } else {
                if (applinkStatus.getError() != null && CONNECTION_REFUSED.equals(applinkStatus.getError().getType())) {
                    applicationLinkBean.setStatus(UNAVAILABLE);
                } else {
                    applicationLinkBean.setStatus(CONFIGURATION_ERROR);
                }
            }
        } catch (NoAccessException | NoSuchApplinkException e) {
            applicationLinkBean.setStatus(CONFIGURATION_ERROR);
        }

        return applicationLinkBean;
    }

}

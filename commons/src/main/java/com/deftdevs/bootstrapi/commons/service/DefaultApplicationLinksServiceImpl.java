package com.deftdevs.bootstrapi.commons.service;

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
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.exception.NoAccessException;
import com.atlassian.applinks.internal.common.exception.NoSuchApplinkException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.applinks.spi.link.MutableApplicationLink;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType;
import com.deftdevs.bootstrapi.commons.model.util.ApplicationLinkModelUtil;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.CONNECTION_REFUSED;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.*;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.CONFIGURATION_ERROR;

public class DefaultApplicationLinksServiceImpl implements ApplicationLinksService {

    private static final Logger log = LoggerFactory.getLogger(DefaultApplicationLinksServiceImpl.class);

    private final MutatingApplicationLinkService mutatingApplicationLinkService;

    private final ApplinkStatusService applinkStatusService;

    private final TypeAccessor typeAccessor;

    private final ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper;

    public DefaultApplicationLinksServiceImpl(
            final MutatingApplicationLinkService mutatingApplicationLinkService,
            final ApplinkStatusService applinkStatusService,
            final TypeAccessor typeAccessor,
            final ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper) {

        this.mutatingApplicationLinkService = mutatingApplicationLinkService;
        this.applinkStatusService = applinkStatusService;
        this.typeAccessor = typeAccessor;
        this.applicationLinksAuthConfigHelper = applicationLinksAuthConfigHelper;
    }

    @Override
    public List<ApplicationLinkModel> getApplicationLinks() {
        final Iterable<ApplicationLink> applicationLinksIterable = mutatingApplicationLinkService.getApplicationLinks();

        return StreamSupport.stream(applicationLinksIterable.spliterator(),false)
                .map(this::getApplicationLinkModel)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationLinkModel getApplicationLink(
            final UUID uuid) {

        final ApplicationId id = new ApplicationId(uuid.toString());

        try {
            final MutableApplicationLink applicationLink = mutatingApplicationLinkService.getApplicationLink(id);

            if (applicationLink == null) {
                throw new NotFoundException(String.format("Application link with ID '%s' was not found!", id));
            }

            return getApplicationLinkModel(applicationLink);
        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<ApplicationLinkModel> setApplicationLinks(
            final List<ApplicationLinkModel> applicationLinkModels,
            final boolean ignoreSetupErrors) {

        // existing application links map
        final Map<URI, ApplicationLinkModel> linkModelMap = getApplicationLinks().stream()
                .collect(Collectors.toMap(ApplicationLinkModel::getRpcUrl, link -> link));

        // find existing link by rpcUrl
        for (ApplicationLinkModel applicationLink : applicationLinkModels) {
            URI key = applicationLink.getRpcUrl();
            if (linkModelMap.containsKey(key)) {
                setApplicationLink(linkModelMap.get(key).getUuid(), applicationLink, ignoreSetupErrors);
            } else {
                addApplicationLink(applicationLink, ignoreSetupErrors);
            }
        }

        return getApplicationLinks();
    }

    @Override
    public ApplicationLinkModel setApplicationLink(
            final UUID uuid,
            final ApplicationLinkModel applicationLinkModel,
            final boolean ignoreSetupErrors) {

        final ApplicationId applicationId = new ApplicationId(uuid.toString());

        try {
            final MutableApplicationLink applicationLink = mutatingApplicationLinkService.getApplicationLink(applicationId);
            final OAuthConfig outgoingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getOutgoingAuthType());
            final OAuthConfig incomingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getIncomingAuthType());
            final ApplicationType applicationType = buildApplicationType(applicationLinkModel.getType());
            final ApplicationLinkDetails applicationLinkDetails = ApplicationLinkModelUtil.toApplicationLinkDetails(applicationLinkModel);

            // entity must be removed first (there is no update method that can change types)
            mutatingApplicationLinkService.deleteApplicationLink(applicationLink);

            // then a new entity is added with the known existing application ID (UUID)
            final MutableApplicationLink recreatedApplicationLink = mutatingApplicationLinkService.addApplicationLink(applicationId, applicationType, applicationLinkDetails);

            // configuring authentication might fail if setup is incorrect or remote app is unavailable
            setOutgoingOAuthConfig(applicationLink, outgoingOAuthConfig);
            setIncomingOAuthConfig(applicationLink, incomingOAuthConfig, ignoreSetupErrors);

            return getApplicationLinkModel(recreatedApplicationLink);
        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public ApplicationLinkModel addApplicationLink(
            final ApplicationLinkModel applicationLinkModel,
            final boolean ignoreSetupErrors) {

        final ApplicationLinkDetails applicationLinkDetails = ApplicationLinkModelUtil.toApplicationLinkDetails(applicationLinkModel);
        final OAuthConfig outgoingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getOutgoingAuthType());
        final OAuthConfig incomingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getIncomingAuthType());
        final ApplicationType applicationType = buildApplicationType(applicationLinkModel.getType());

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
            applicationLink = mutatingApplicationLinkService.createApplicationLink(applicationType, applicationLinkDetails);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

        // configuring authentication might fail if setup is incorrect or remote app is unavailable
        setOutgoingOAuthConfig(applicationLink, outgoingOAuthConfig);
        setIncomingOAuthConfig(applicationLink, incomingOAuthConfig, ignoreSetupErrors);

        return getApplicationLinkModel(applicationLink);
    }

    @Override
    public void deleteApplicationLinks(boolean force) {
        if (!force) {
            throw new BadRequestException("'force = true' must be supplied to delete all entries");
        } else {
            for (ApplicationLink applicationLink : mutatingApplicationLinkService.getApplicationLinks()) {
                mutatingApplicationLinkService.deleteApplicationLink(applicationLink);
            }
        }
    }

    @Override
    public void deleteApplicationLink(UUID id) {
        ApplicationId applicationId = new ApplicationId(String.valueOf(id));
        try {
            MutableApplicationLink applicationLink = mutatingApplicationLinkService.getApplicationLink(applicationId);
            mutatingApplicationLinkService.deleteApplicationLink(applicationLink);
        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    protected ApplicationType buildApplicationType(ApplicationLinkType linkType) {
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

    protected OAuthConfig getOutgoingOAuthConfig(
            final ApplicationLink applicationLink) {

        return applicationLinksAuthConfigHelper.getOutgoingOAuthConfig(applicationLink);
    }

    protected void setOutgoingOAuthConfig(
            final ApplicationLink applicationLink,
            final OAuthConfig outgoingOAuthConfig) {

        applicationLinksAuthConfigHelper.setOutgoingOAuthConfig(applicationLink, outgoingOAuthConfig);
    }

    protected OAuthConfig getIncomingOAuthConfig(
            final ApplicationLink applicationLink) {

        return applicationLinksAuthConfigHelper.getIncomingOAuthConfig(applicationLink);
    }

    protected void setIncomingOAuthConfig(
            final ApplicationLink applicationLink,
            final OAuthConfig incomingOAuthConfig,
            final boolean ignoreSetupErrors) {

        try {
            applicationLinksAuthConfigHelper.setIncomingOAuthConfig(applicationLink, incomingOAuthConfig);
        } catch (ConsumerInformationUnavailableException e) {
            if (!ignoreSetupErrors) {
                throw new BadRequestException(e.getMessage());
            }
        }
    }

    protected ApplicationLinkModel.ApplicationLinkStatus getStatus(
            final ApplicationLink applicationLink) {

        try {
            final ApplinkStatus applinkStatus = applinkStatusService.getApplinkStatus(applicationLink.getId());

            if (applinkStatus.isWorking()) {
                return AVAILABLE;
            } else if (applinkStatus.getError() != null && applinkStatus.getError().getType().equals(CONNECTION_REFUSED)) {
                return UNAVAILABLE;
            }
        } catch (NoAccessException | NoSuchApplinkException ignored) {}

        return CONFIGURATION_ERROR;
    }

    private ApplicationLinkModel getApplicationLinkModel(
            final ApplicationLink applicationLink) {

        return ApplicationLinkModelUtil.toApplicationLinkModel(
                applicationLink,
                getOutgoingOAuthConfig(applicationLink),
                getIncomingOAuthConfig(applicationLink),
                getStatus(applicationLink));
    }
}

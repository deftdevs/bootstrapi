package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.applinks.api.auth.AuthenticationProvider;
import com.atlassian.applinks.api.application.bamboo.BambooApplicationType;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType;
import com.atlassian.applinks.api.application.crowd.CrowdApplicationType;
import com.atlassian.applinks.api.application.fecru.FishEyeCrucibleApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.applinks.core.ApplinkStatus;
import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.internal.common.auth.oauth.ApplinksOAuth;
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.exception.NoAccessException;
import com.atlassian.applinks.internal.common.exception.NoSuchApplinkException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.spi.application.ApplicationIdUtil;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.CONNECTION_REFUSED;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.AVAILABLE;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.CONFIGURATION_ERROR;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.UNAVAILABLE;

public class DefaultApplicationLinksServiceImpl implements ApplicationLinksService {

    private static final Logger log = LoggerFactory.getLogger(DefaultApplicationLinksServiceImpl.class);

    // applinks' DefaultApplicationLinkService throws TypeNotInstalledException with this
    // literal as the type when a link's type property is missing entirely ("Link is
    // corrupted"), as opposed to the actual type ID of a type that is just not installed;
    // there is no public applinks constant for it
    private static final String TYPE_ID_UNKNOWN = "unknown";

    private static final String NOT_FOUND_MESSAGE = "Application link with ID '%s' was not found!";

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
    public Map<String, ApplicationLinkModel> getApplicationLinks() {
        final Iterable<ApplicationLink> applicationLinksIterable = mutatingApplicationLinkService.getApplicationLinks();

        return StreamSupport.stream(applicationLinksIterable.spliterator(),false)
                .map(this::getApplicationLinkModel)
                .collect(Collectors.toMap(ApplicationLinkModel::getName, Function.identity()));
    }

    @Override
    public ApplicationLinkModel getApplicationLink(
            final UUID uuid) {

        final ApplicationId id = new ApplicationId(uuid.toString());

        try {
            final MutableApplicationLink applicationLink = mutatingApplicationLinkService.getApplicationLink(id);

            if (applicationLink == null) {
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, id));
            }

            return getApplicationLinkModel(applicationLink);
        } catch (TypeNotInstalledException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Map<String, ApplicationLinkModel> setApplicationLinks(
            final Map<String, ApplicationLinkModel> applicationLinkModels) {

        // existing application links map
        final Map<String, ApplicationLinkModel> linkModelMap = getApplicationLinks();

        // find existing link by name
        for (Map.Entry<String, ApplicationLinkModel> applicationLinkModelEntry : applicationLinkModels.entrySet()) {
            final String name = applicationLinkModelEntry.getKey();
            final ApplicationLinkModel applicationLinkModel = applicationLinkModelEntry.getValue();

            // declarative no-op: null model means "leave this entry untouched"
            if (applicationLinkModel == null) {
                continue;
            }

            if (applicationLinkModel.getName() == null) {
                applicationLinkModel.setName(name);
            }

            if (linkModelMap.containsKey(name)) {
                setApplicationLink(linkModelMap.get(name).getUuid(), applicationLinkModel);
            } else {
                addApplicationLink(applicationLinkModel);
            }
        }

        return getApplicationLinks();
    }

    @Override
    public ApplicationLinkModel setApplicationLink(
            final UUID uuid,
            final ApplicationLinkModel applicationLinkModel) {

        final ApplicationId applicationId = new ApplicationId(uuid.toString());

        final OAuthConfig outgoingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getOutgoingAuthType());
        final OAuthConfig incomingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getIncomingAuthType());
        final ApplicationType applicationType = buildApplicationType(applicationLinkModel.getType());
        final ApplicationLinkDetails applicationLinkDetails = ApplicationLinkModelUtil.toApplicationLinkDetails(applicationLinkModel);

        MutableApplicationLink applicationLink = null;
        boolean corrupted = false;

        try {
            applicationLink = mutatingApplicationLinkService.getApplicationLink(applicationId);
        } catch (TypeNotInstalledException e) {
            // a valid link whose type module is just not installed must not be purged
            if (!isCorrupted(e)) {
                throw new BadRequestException(e.getMessage());
            }

            corrupted = true;
        }

        // keep the original state so the link can be restored if the recreation below fails
        ApplicationType originalApplicationType = null;
        ApplicationLinkDetails originalApplicationLinkDetails = null;
        OAuthConfig originalOutgoingOAuthConfig = null;
        Object originalIncomingConsumerKey = null;

        // entity must be removed first (there is no update method that can change types)
        if (applicationLink != null) {
            originalApplicationType = applicationLink.getType();
            originalApplicationLinkDetails = ApplicationLinkDetails.builder()
                    .name(applicationLink.getName())
                    .displayUrl(applicationLink.getDisplayUrl())
                    .rpcUrl(applicationLink.getRpcUrl())
                    .isPrimary(applicationLink.isPrimary())
                    .build();
            originalOutgoingOAuthConfig = getOutgoingOAuthConfig(applicationLink);
            originalIncomingConsumerKey = applicationLink.getProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY);
            mutatingApplicationLinkService.deleteApplicationLink(applicationLink);
        } else if (corrupted) {
            log.warn("Removing corrupted application link '{}' before recreating it", applicationId);
            deleteCorruptedApplicationLink(applicationId);
        } else {
            throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, applicationId));
        }

        // then a new entity is added with the known existing application ID (UUID);
        // if that fails, restore the original entity, otherwise the instance is left
        // with a registered application link ID without properties ("Link is corrupted")
        final MutableApplicationLink recreatedApplicationLink;
        try {
            recreatedApplicationLink = mutatingApplicationLinkService.addApplicationLink(applicationId, applicationType, applicationLinkDetails);
        } catch (Exception e) {
            if (originalApplicationLinkDetails != null) {
                restoreApplicationLink(applicationId, originalApplicationType, originalApplicationLinkDetails,
                        originalOutgoingOAuthConfig, originalIncomingConsumerKey);
            }
            throw new BadRequestException(e.getMessage());
        }

        // configuring authentication might fail if setup is incorrect or remote app is unavailable
        setOutgoingOAuthConfig(recreatedApplicationLink, outgoingOAuthConfig);
        setIncomingOAuthConfig(recreatedApplicationLink, incomingOAuthConfig, Boolean.TRUE.equals(applicationLinkModel.getIgnoreSetupErrors()));

        return getApplicationLinkModel(recreatedApplicationLink);
    }

    @Override
    public ApplicationLinkModel addApplicationLink(
            final ApplicationLinkModel applicationLinkModel) {

        final ApplicationLinkDetails applicationLinkDetails = ApplicationLinkModelUtil.toApplicationLinkDetails(applicationLinkModel);
        final OAuthConfig outgoingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getOutgoingAuthType());
        final OAuthConfig incomingOAuthConfig = ApplicationLinkModelUtil.toOAuthConfig(applicationLinkModel.getIncomingAuthType());
        final ApplicationType applicationType = buildApplicationType(applicationLinkModel.getType());

        //check if there is already an application link of supplied type and if yes, remove it
        ApplicationLink primaryApplicationLink = mutatingApplicationLinkService.getPrimaryApplicationLink(applicationType.getClass());
        if (primaryApplicationLink != null) {
            log.info("An existing application link configuration '{}' was found and is removed now before adding the new configuration",
                    primaryApplicationLink.getName());
            mutatingApplicationLinkService.deleteApplicationLink(primaryApplicationLink);
        }

        // a corrupted remnant registered under the ID derived from the rpc URL would
        // make the creation below fail, so it must be removed first
        if (applicationLinkModel.getRpcUrl() != null) {
            final ApplicationId generatedApplicationId = ApplicationIdUtil.generate(applicationLinkModel.getRpcUrl());

            try {
                mutatingApplicationLinkService.getApplicationLink(generatedApplicationId);
            } catch (TypeNotInstalledException e) {
                // a valid link whose type module is just not installed must not be purged;
                // the creation below will then fail with a meaningful error message
                if (isCorrupted(e)) {
                    log.warn("Removing corrupted application link '{}' before creating a new link for URL '{}'",
                            generatedApplicationId, applicationLinkModel.getRpcUrl());
                    deleteCorruptedApplicationLink(generatedApplicationId);
                }
            }
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
        setIncomingOAuthConfig(applicationLink, incomingOAuthConfig, Boolean.TRUE.equals(applicationLinkModel.getIgnoreSetupErrors()));

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

            if (applicationLink == null) {
                throw new NotFoundException(String.format(NOT_FOUND_MESSAGE, applicationId));
            }

            mutatingApplicationLinkService.deleteApplicationLink(applicationLink);
        } catch (TypeNotInstalledException e) {
            // the ID is registered, but the link cannot be retrieved because its type
            // property is missing ("Link is corrupted") or its type module is not installed
            log.warn("Deleting application link '{}' that cannot be retrieved: {}", applicationId, e.getMessage());
            deleteCorruptedApplicationLink(applicationId);
        }
    }

    // best-effort restore of a just-deleted application link after its recreation
    // failed, including the authentication state managed by this service - properties
    // of other authentication providers cannot be restored
    private void restoreApplicationLink(
            final ApplicationId applicationId,
            final ApplicationType originalApplicationType,
            final ApplicationLinkDetails originalApplicationLinkDetails,
            final OAuthConfig originalOutgoingOAuthConfig,
            final Object originalIncomingConsumerKey) {

        try {
            final MutableApplicationLink restoredApplicationLink = mutatingApplicationLinkService.addApplicationLink(applicationId, originalApplicationType, originalApplicationLinkDetails);

            if (originalOutgoingOAuthConfig != null) {
                setOutgoingOAuthConfig(restoredApplicationLink, originalOutgoingOAuthConfig);
            }

            if (originalIncomingConsumerKey != null) {
                restoredApplicationLink.putProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY, originalIncomingConsumerKey);
            }
        } catch (Exception restoreException) {
            log.error("Failed to restore application link '{}' after its recreation failed; the link may be corrupted now", applicationId, restoreException);
        }
    }

    private static boolean isCorrupted(final TypeNotInstalledException e) {
        return TYPE_ID_UNKNOWN.equals(e.getType());
    }

    // a corrupted link cannot be retrieved through the applinks API, but deletion only
    // requires the ID, so a minimal link implementation is enough to get it removed
    private void deleteCorruptedApplicationLink(ApplicationId applicationId) {
        mutatingApplicationLinkService.deleteApplicationLink(new CorruptedApplicationLink(applicationId));
    }

    private static class CorruptedApplicationLink implements ApplicationLink {

        private final ApplicationId id;

        private CorruptedApplicationLink(final ApplicationId id) {
            this.id = id;
        }

        @Override
        public ApplicationId getId() {
            return id;
        }

        @Override
        public ApplicationType getType() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public URI getDisplayUrl() {
            return null;
        }

        @Override
        public URI getRpcUrl() {
            return null;
        }

        @Override
        public boolean isPrimary() {
            return false;
        }

        @Override
        public boolean isSystem() {
            return false;
        }

        @Override
        public Object getProperty(String key) {
            return null;
        }

        @Override
        public Object putProperty(String key, Object value) {
            return null;
        }

        @Override
        public Object removeProperty(String key) {
            return null;
        }

        @Override
        public ApplicationLinkRequestFactory createAuthenticatedRequestFactory() {
            return null;
        }

        @Override
        public ApplicationLinkRequestFactory createAuthenticatedRequestFactory(Class<? extends AuthenticationProvider> providerClass) {
            return null;
        }

        @Override
        public ApplicationLinkRequestFactory createImpersonatingAuthenticatedRequestFactory() {
            return null;
        }

        @Override
        public ApplicationLinkRequestFactory createNonImpersonatingAuthenticatedRequestFactory() {
            return null;
        }
    }

    protected ApplicationType buildApplicationType(ApplicationLinkType linkType) {
        final ApplicationType applicationType;

        switch (linkType) {
            case BAMBOO:
                applicationType = typeAccessor.getApplicationType(BambooApplicationType.class);
                break;
            case JIRA:
                applicationType = typeAccessor.getApplicationType(JiraApplicationType.class);
                break;
            case BITBUCKET:
                applicationType = typeAccessor.getApplicationType(BitbucketApplicationType.class);
                break;
            case CONFLUENCE:
                applicationType = typeAccessor.getApplicationType(ConfluenceApplicationType.class);
                break;
            case FISHEYE:
                applicationType = typeAccessor.getApplicationType(FishEyeCrucibleApplicationType.class);
                break;
            case CROWD:
                applicationType = typeAccessor.getApplicationType(CrowdApplicationType.class);
                break;
            default:
                throw new NotImplementedException("application type '" + linkType + "' not implemented");
        }

        if (applicationType == null) {
            throw new BadRequestException(String.format("application type '%s' is not installed", linkType));
        }

        return applicationType;
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

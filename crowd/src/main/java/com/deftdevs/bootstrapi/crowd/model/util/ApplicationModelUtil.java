package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.model.application.*;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationModelUtil {

    public static ApplicationModel toApplicationModel(
            final Application application,
            final DefaultGroupMembershipService defaultGroupMembershipService) {

        final ApplicationModel applicationModel = new ApplicationModel();

        applicationModel.setId(application.getId());
        applicationModel.setName(application.getName());
        applicationModel.setDescription(application.getDescription());
        applicationModel.setActive(application.isActive());
        applicationModel.setType(toApplicationModelType(application.getType()));
        applicationModel.setCachedDirectoriesAuthenticationOrderOptimisationEnabled(application.isCachedDirectoriesAuthenticationOrderOptimisationEnabled());
        applicationModel.setDirectoryMappings(toApplicationModelDirectoryMappings(application, defaultGroupMembershipService).stream()
                .sorted(Comparator.comparing(ApplicationModel.ApplicationDirectoryMapping::getDirectoryName))
                .collect(Collectors.toList()));
        applicationModel.setAccessBasedSynchronisation(toApplicationModelAccessBasedSynchronisation(application));
        applicationModel.setMembershipAggregationEnabled(application.isMembershipAggregationEnabled());
        applicationModel.setRemoteAddresses(toStringCollection(application.getRemoteAddresses()));
        applicationModel.setAliasingEnabled(application.isAliasingEnabled());
        applicationModel.setLowercaseOutputEnabled(application.isLowerCaseOutput());
        applicationModel.setAuthenticationWithoutPasswordEnabled(application.isAuthenticationWithoutPasswordEnabled());

        return applicationModel;
    }

    public static Application toApplication(
            final ApplicationModel applicationModel,
            final Application existingApplication) {

        final ImmutableApplication.Builder applicationBuilder = new ImmutableApplication.Builder(existingApplication);
        return toApplicationInternal(applicationModel, applicationBuilder);
    }

    public static Application toApplication(
            final ApplicationModel applicationModel) {

        // don't set id from request data
        final ImmutableApplication.Builder applicationBuilder = ImmutableApplication.builder(applicationModel.getName(), toApplicationType(applicationModel.getType()));
        return toApplicationInternal(applicationModel, applicationBuilder);
    }

    private static Application toApplicationInternal(
            final ApplicationModel applicationModel,
            final ImmutableApplication.Builder applicationBuilder) {

        // we need to run null checks everywhere because the application builder doesn't really support setting null...

        if (applicationModel.getId() != null) {
            applicationBuilder.setId(applicationModel.getId());
        }

        if (applicationModel.getName() != null) {
            applicationBuilder.setName(applicationModel.getName());
        }

        if (applicationModel.getDescription() != null) {
            applicationBuilder.setDescription(applicationModel.getDescription());
        }

        if (applicationModel.getActive() != null) {
            applicationBuilder.setActive(applicationModel.getActive());
        }

        // updating the application type is not possible, and error handling is difficult here,
        // because the application builder has no getters, so see end of method

        if (applicationModel.getPassword() != null) {
            applicationBuilder.setPasswordCredential(PasswordCredential.unencrypted(applicationModel.getPassword()));
        }

        if (applicationModel.getCachedDirectoriesAuthenticationOrderOptimisationEnabled() != null) {
            applicationBuilder.setCachedDirectoriesAuthenticationOrderOptimisationEnabled(applicationModel.getCachedDirectoriesAuthenticationOrderOptimisationEnabled());
        }

        // setting directory mappings is pointless because they are not respected when adding / updating applications

        if (applicationModel.getAccessBasedSynchronisation() != null) {
            applicationBuilder.setFilteringGroupsWithAccessEnabled(
                    applicationModel.getAccessBasedSynchronisation() == ApplicationModel.AccessBasedSynchronisation.USER_AND_GROUP_FILTERING);
            applicationBuilder.setFilteringUsersWithAccessEnabled(
                    applicationModel.getAccessBasedSynchronisation() == ApplicationModel.AccessBasedSynchronisation.USER_AND_GROUP_FILTERING
                            || applicationModel.getAccessBasedSynchronisation() == ApplicationModel.AccessBasedSynchronisation.USER_ONLY_FILTERING);
        }

        if (applicationModel.getMembershipAggregationEnabled() != null) {
            applicationBuilder.setMembershipAggregationEnabled(applicationModel.getMembershipAggregationEnabled());
        }

        if (applicationModel.getRemoteAddresses() != null) {
            applicationBuilder.setRemoteAddresses(toAddressSet(applicationModel.getRemoteAddresses()));
        }

        if (applicationModel.getAliasingEnabled() != null) {
            applicationBuilder.setAliasingEnabled(applicationModel.getAliasingEnabled());
        }

        if (applicationModel.getLowercaseOutputEnabled() != null) {
            applicationBuilder.setLowercaseOutput(applicationModel.getLowercaseOutputEnabled());
        }

        if (applicationModel.getAuthenticationWithoutPasswordEnabled() != null) {
            applicationBuilder.setAuthenticationWithoutPasswordEnabled(applicationModel.getAuthenticationWithoutPasswordEnabled());
        }

        final Application application = applicationBuilder.build();

        if (applicationModel.getType() != null && application.getType() != toApplicationType(applicationModel.getType())) {
            throw new BadRequestException("Changing the application type is not allowed");
        }

        return application;
    }

    @Nullable
    public static ApplicationModel.ApplicationType toApplicationModelType(
            final ApplicationType applicationType) {

        if (ApplicationType.GENERIC_APPLICATION.equals(applicationType)) {
            return ApplicationModel.ApplicationType.GENERIC;
        } else if (ApplicationType.PLUGIN.equals(applicationType)) {
            return ApplicationModel.ApplicationType.PLUGIN;
        } else if (ApplicationType.CROWD.equals(applicationType)) {
            return ApplicationModel.ApplicationType.CROWD;
        } else if (ApplicationType.JIRA.equals(applicationType)) {
            return ApplicationModel.ApplicationType.JIRA;
        } else if (ApplicationType.CONFLUENCE.equals(applicationType)) {
            return ApplicationModel.ApplicationType.CONFLUENCE;
        } else if (ApplicationType.STASH.equals(applicationType)) {
            return ApplicationModel.ApplicationType.BITBUCKET;
        } else if (ApplicationType.FISHEYE.equals(applicationType)) {
            return ApplicationModel.ApplicationType.FISHEYE;
        } else if (ApplicationType.CRUCIBLE.equals(applicationType)) {
            return ApplicationModel.ApplicationType.CRUCIBLE;
        } else if (ApplicationType.BAMBOO.equals(applicationType)) {
            return ApplicationModel.ApplicationType.BAMBOO;
        }

        return null;
    }

    @Nullable
    public static ApplicationType toApplicationType(
            final ApplicationModel.ApplicationType applicationModelType) {

        if (ApplicationModel.ApplicationType.GENERIC.equals(applicationModelType)) {
            return ApplicationType.GENERIC_APPLICATION;
        } else if (ApplicationModel.ApplicationType.PLUGIN.equals(applicationModelType)) {
            return ApplicationType.PLUGIN;
        } else if (ApplicationModel.ApplicationType.CROWD.equals(applicationModelType)) {
            return ApplicationType.CROWD;
        } else if (ApplicationModel.ApplicationType.JIRA.equals(applicationModelType)) {
            return ApplicationType.JIRA;
        } else if (ApplicationModel.ApplicationType.CONFLUENCE.equals(applicationModelType)) {
            return ApplicationType.CONFLUENCE;
        } else if (ApplicationModel.ApplicationType.BITBUCKET.equals(applicationModelType)) {
            return ApplicationType.STASH;
        } else if (ApplicationModel.ApplicationType.FISHEYE.equals(applicationModelType)) {
            return ApplicationType.FISHEYE;
        } else if (ApplicationModel.ApplicationType.CRUCIBLE.equals(applicationModelType)) {
            return ApplicationType.CRUCIBLE;
        } else if (ApplicationModel.ApplicationType.BAMBOO.equals(applicationModelType)) {
            return ApplicationType.BAMBOO;
        }

        return null;
    }

    @Nonnull
    static List<ApplicationModel.ApplicationDirectoryMapping> toApplicationModelDirectoryMappings(
            @Nonnull final Application application,
            @Nonnull final DefaultGroupMembershipService defaultGroupMembershipService) {

        final List<ApplicationDirectoryMapping> applicationDirectoryMappings = application.getApplicationDirectoryMappings();
        final List<ApplicationModel.ApplicationDirectoryMapping> applicationModelDirectoryMappings = new ArrayList<>();

        for (final ApplicationDirectoryMapping applicationDirectoryMapping : applicationDirectoryMappings) {
            final ApplicationModel.ApplicationDirectoryMapping applicationModelDirectoryMapping = new ApplicationModel.ApplicationDirectoryMapping();
            applicationModelDirectoryMapping.setDirectoryName(applicationDirectoryMapping.getDirectory().getName());
            applicationModelDirectoryMapping.setAuthenticationAllowAll(applicationDirectoryMapping.isAllowAllToAuthenticate());

            // if all directory users are allowed to authenticate, we don't return the unused list of groups that are allowed to do so,
            // but instead we just return an empty list
            if (!applicationDirectoryMapping.isAllowAllToAuthenticate()) {
                applicationModelDirectoryMapping.setAuthenticationGroups(new ArrayList<>(applicationDirectoryMapping.getAuthorisedGroupNames()));
            } else {
                applicationModelDirectoryMapping.setAuthenticationGroups(Collections.emptyList());
            }

            try {
                applicationModelDirectoryMapping.setAutoAssignmentGroups(defaultGroupMembershipService.listAll(application,
                        applicationDirectoryMapping).stream().sorted().collect(Collectors.toList()));
            } catch (OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }

            applicationModelDirectoryMapping.setAllowedOperations(applicationDirectoryMapping.getAllowedOperations().stream().sorted().collect(Collectors.toList()));

            applicationModelDirectoryMappings.add(applicationModelDirectoryMapping);
        }

        return applicationModelDirectoryMappings;
    }

    static ApplicationModel.AccessBasedSynchronisation toApplicationModelAccessBasedSynchronisation(
            final Application application) {

        // the case that filtering groups with access is enabled without
        // filtering users with access is enabled also should not exist
        if (application.isFilteringGroupsWithAccessEnabled() && application.isFilteringUsersWithAccessEnabled()) {
            return ApplicationModel.AccessBasedSynchronisation.USER_AND_GROUP_FILTERING;
        }

        // so if filtering groups with access is not enabled, there are only two cases left
        if (application.isFilteringUsersWithAccessEnabled()) {
            return ApplicationModel.AccessBasedSynchronisation.USER_ONLY_FILTERING;
        }

        return ApplicationModel.AccessBasedSynchronisation.NO_FILTERING;
    }

    @Nonnull
    static Set<RemoteAddress> toAddressSet(
            @Nullable final List<String> remoteAddresses) {

        if (remoteAddresses == null) {
            return new HashSet<>();
        }

        return remoteAddresses.stream()
                .map(RemoteAddress::new)
                .collect(Collectors.toSet());
    }

    @Nonnull
    public static List<String> toStringCollection(
            @Nonnull final Set<RemoteAddress> remoteAddresses) {

        return remoteAddresses.stream()
                .map(RemoteAddress::getAddress)
                .sorted()
                .collect(Collectors.toList());
    }

    private ApplicationModelUtil() {
    }
}

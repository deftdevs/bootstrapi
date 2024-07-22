package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.model.application.*;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationBeanUtil {

    public static ApplicationBean toApplicationBean(
            final Application application,
            final DefaultGroupMembershipService defaultGroupMembershipService) {

        final ApplicationBean applicationBean = new ApplicationBean();

        applicationBean.setId(application.getId());
        applicationBean.setName(application.getName());
        applicationBean.setDescription(application.getDescription());
        applicationBean.setActive(application.isActive());
        applicationBean.setType(toApplicationBeanType(application.getType()));
        applicationBean.setCachedDirectoriesAuthenticationOrderOptimisationEnabled(application.isCachedDirectoriesAuthenticationOrderOptimisationEnabled());
        applicationBean.setDirectoryMappings(toApplicationBeanDirectoryMappings(application, defaultGroupMembershipService).stream()
                .sorted(Comparator.comparing(ApplicationBean.ApplicationDirectoryMapping::getDirectoryName))
                .collect(Collectors.toList()));
        applicationBean.setAccessBasedSynchronisation(toApplicationBeanAccessBasedSynchronisation(application));
        applicationBean.setMembershipAggregationEnabled(application.isMembershipAggregationEnabled());
        applicationBean.setRemoteAddresses(toStringCollection(application.getRemoteAddresses()));
        applicationBean.setAliasingEnabled(application.isAliasingEnabled());
        applicationBean.setLowercaseOutputEnabled(application.isLowerCaseOutput());
        applicationBean.setAuthenticationWithoutPasswordEnabled(application.isAuthenticationWithoutPasswordEnabled());

        return applicationBean;
    }

    public static Application toApplication(
            final ApplicationBean applicationBean,
            final Application existingApplication) {

        final ImmutableApplication.Builder applicationBuilder = new ImmutableApplication.Builder(existingApplication);
        return toApplicationInternal(applicationBean, applicationBuilder);
    }

    public static Application toApplication(
            final ApplicationBean applicationBean) {

        // don't set id from request data
        final ImmutableApplication.Builder applicationBuilder = ImmutableApplication.builder(applicationBean.getName(), toApplicationType(applicationBean.getType()));
        return toApplicationInternal(applicationBean, applicationBuilder);
    }

    private static Application toApplicationInternal(
            final ApplicationBean applicationBean,
            final ImmutableApplication.Builder applicationBuilder) {

        // we need to run null checks everywhere because the application builder doesn't really support setting null...

        if (applicationBean.getId() != null) {
            applicationBuilder.setId(applicationBean.getId());
        }

        if (applicationBean.getName() != null) {
            applicationBuilder.setName(applicationBean.getName());
        }

        if (applicationBean.getDescription() != null) {
            applicationBuilder.setDescription(applicationBean.getDescription());
        }

        if (applicationBean.getActive() != null) {
            applicationBuilder.setActive(applicationBean.getActive());
        }

        // updating the application type is not possible, and error handling is difficult here,
        // because the application builder has no getters, so see end of method

        if (applicationBean.getPassword() != null) {
            applicationBuilder.setPasswordCredential(PasswordCredential.unencrypted(applicationBean.getPassword()));
        }

        if (applicationBean.getCachedDirectoriesAuthenticationOrderOptimisationEnabled() != null) {
            applicationBuilder.setCachedDirectoriesAuthenticationOrderOptimisationEnabled(applicationBean.getCachedDirectoriesAuthenticationOrderOptimisationEnabled());
        }

        // setting directory mappings is pointless because they are not respected when adding / updating applications

        if (applicationBean.getAccessBasedSynchronisation() != null) {
            applicationBuilder.setFilteringGroupsWithAccessEnabled(
                    applicationBean.getAccessBasedSynchronisation() == ApplicationBean.AccessBasedSynchronisation.USER_AND_GROUP_FILTERING);
            applicationBuilder.setFilteringUsersWithAccessEnabled(
                    applicationBean.getAccessBasedSynchronisation() == ApplicationBean.AccessBasedSynchronisation.USER_AND_GROUP_FILTERING
                            || applicationBean.getAccessBasedSynchronisation() == ApplicationBean.AccessBasedSynchronisation.USER_ONLY_FILTERING);
        }

        if (applicationBean.getMembershipAggregationEnabled() != null) {
            applicationBuilder.setMembershipAggregationEnabled(applicationBean.getMembershipAggregationEnabled());
        }

        if (applicationBean.getRemoteAddresses() != null) {
            applicationBuilder.setRemoteAddresses(toAddressSet(applicationBean.getRemoteAddresses()));
        }

        if (applicationBean.getAliasingEnabled() != null) {
            applicationBuilder.setAliasingEnabled(applicationBean.getAliasingEnabled());
        }

        if (applicationBean.getLowercaseOutputEnabled() != null) {
            applicationBuilder.setLowercaseOutput(applicationBean.getLowercaseOutputEnabled());
        }

        if (applicationBean.getAuthenticationWithoutPasswordEnabled() != null) {
            applicationBuilder.setAuthenticationWithoutPasswordEnabled(applicationBean.getAuthenticationWithoutPasswordEnabled());
        }

        final Application application = applicationBuilder.build();

        if (applicationBean.getType() != null && application.getType() != toApplicationType(applicationBean.getType())) {
            throw new BadRequestException("Changing the application type is not allowed");
        }

        return application;
    }

    @Nullable
    public static ApplicationBean.ApplicationType toApplicationBeanType(
            final ApplicationType applicationType) {

        if (ApplicationType.GENERIC_APPLICATION.equals(applicationType)) {
            return ApplicationBean.ApplicationType.GENERIC;
        } else if (ApplicationType.PLUGIN.equals(applicationType)) {
            return ApplicationBean.ApplicationType.PLUGIN;
        } else if (ApplicationType.CROWD.equals(applicationType)) {
            return ApplicationBean.ApplicationType.CROWD;
        } else if (ApplicationType.JIRA.equals(applicationType)) {
            return ApplicationBean.ApplicationType.JIRA;
        } else if (ApplicationType.CONFLUENCE.equals(applicationType)) {
            return ApplicationBean.ApplicationType.CONFLUENCE;
        } else if (ApplicationType.STASH.equals(applicationType)) {
            return ApplicationBean.ApplicationType.BITBUCKET;
        } else if (ApplicationType.FISHEYE.equals(applicationType)) {
            return ApplicationBean.ApplicationType.FISHEYE;
        } else if (ApplicationType.CRUCIBLE.equals(applicationType)) {
            return ApplicationBean.ApplicationType.CRUCIBLE;
        } else if (ApplicationType.BAMBOO.equals(applicationType)) {
            return ApplicationBean.ApplicationType.BAMBOO;
        }

        return null;
    }

    @Nullable
    public static ApplicationType toApplicationType(
            final ApplicationBean.ApplicationType applicationBeanType) {

        if (ApplicationBean.ApplicationType.GENERIC.equals(applicationBeanType)) {
            return ApplicationType.GENERIC_APPLICATION;
        } else if (ApplicationBean.ApplicationType.PLUGIN.equals(applicationBeanType)) {
            return ApplicationType.PLUGIN;
        } else if (ApplicationBean.ApplicationType.CROWD.equals(applicationBeanType)) {
            return ApplicationType.CROWD;
        } else if (ApplicationBean.ApplicationType.JIRA.equals(applicationBeanType)) {
            return ApplicationType.JIRA;
        } else if (ApplicationBean.ApplicationType.CONFLUENCE.equals(applicationBeanType)) {
            return ApplicationType.CONFLUENCE;
        } else if (ApplicationBean.ApplicationType.BITBUCKET.equals(applicationBeanType)) {
            return ApplicationType.STASH;
        } else if (ApplicationBean.ApplicationType.FISHEYE.equals(applicationBeanType)) {
            return ApplicationType.FISHEYE;
        } else if (ApplicationBean.ApplicationType.CRUCIBLE.equals(applicationBeanType)) {
            return ApplicationType.CRUCIBLE;
        } else if (ApplicationBean.ApplicationType.BAMBOO.equals(applicationBeanType)) {
            return ApplicationType.BAMBOO;
        }

        return null;
    }

    @Nonnull
    static List<ApplicationBean.ApplicationDirectoryMapping> toApplicationBeanDirectoryMappings(
            @Nonnull final Application application,
            @Nonnull final DefaultGroupMembershipService defaultGroupMembershipService) {

        final List<ApplicationDirectoryMapping> applicationDirectoryMappings = application.getApplicationDirectoryMappings();
        final List<ApplicationBean.ApplicationDirectoryMapping> applicationBeanDirectoryMappings = new ArrayList<>();

        for (final ApplicationDirectoryMapping applicationDirectoryMapping : applicationDirectoryMappings) {
            final ApplicationBean.ApplicationDirectoryMapping applicationBeanDirectoryMapping = new ApplicationBean.ApplicationDirectoryMapping();
            applicationBeanDirectoryMapping.setDirectoryName(applicationDirectoryMapping.getDirectory().getName());
            applicationBeanDirectoryMapping.setAuthenticationAllowAll(applicationDirectoryMapping.isAllowAllToAuthenticate());

            // if all directory users are allowed to authenticate, we don't return the unused list of groups that are allowed to do so,
            // but instead we just return an empty list
            if (!applicationDirectoryMapping.isAllowAllToAuthenticate()) {
                applicationBeanDirectoryMapping.setAuthenticationGroups(new ArrayList<>(applicationDirectoryMapping.getAuthorisedGroupNames()));
            } else {
                applicationBeanDirectoryMapping.setAuthenticationGroups(Collections.emptyList());
            }

            try {
                applicationBeanDirectoryMapping.setAutoAssignmentGroups(defaultGroupMembershipService.listAll(application,
                        applicationDirectoryMapping).stream().sorted().collect(Collectors.toList()));
            } catch (OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }

            applicationBeanDirectoryMapping.setAllowedOperations(applicationDirectoryMapping.getAllowedOperations().stream().sorted().collect(Collectors.toList()));

            applicationBeanDirectoryMappings.add(applicationBeanDirectoryMapping);
        }

        return applicationBeanDirectoryMappings;
    }

    static ApplicationBean.AccessBasedSynchronisation toApplicationBeanAccessBasedSynchronisation(
            final Application application) {

        // the case that filtering groups with access is enabled without
        // filtering users with access is enabled also should not exist
        if (application.isFilteringGroupsWithAccessEnabled() && application.isFilteringUsersWithAccessEnabled()) {
            return ApplicationBean.AccessBasedSynchronisation.USER_AND_GROUP_FILTERING;
        }

        // so if filtering groups with access is not enabled, there are only two cases left
        if (application.isFilteringUsersWithAccessEnabled()) {
            return ApplicationBean.AccessBasedSynchronisation.USER_ONLY_FILTERING;
        }

        return ApplicationBean.AccessBasedSynchronisation.NO_FILTERING;
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

    private ApplicationBeanUtil() {
    }
}

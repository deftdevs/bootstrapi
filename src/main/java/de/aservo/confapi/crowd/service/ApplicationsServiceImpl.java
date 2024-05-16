package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.OperationType;
import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.manager.application.ApplicationManager;
import com.atlassian.crowd.manager.application.ApplicationManagerException;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.application.Application;
import com.atlassian.crowd.model.application.ApplicationDirectoryMapping;
import com.atlassian.crowd.model.application.ImmutableApplicationDirectoryMapping;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.crowd.model.ApplicationBean;
import de.aservo.confapi.crowd.model.ApplicationsBean;
import de.aservo.confapi.crowd.model.util.ApplicationBeanUtil;
import de.aservo.confapi.crowd.service.api.ApplicationsService;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ExportAsService(ApplicationsService.class)
public class ApplicationsServiceImpl implements ApplicationsService {

    private final ApplicationManager applicationManager;

    private final DefaultGroupMembershipService defaultGroupMembershipService;

    private final DirectoryManager directoryManager;

    @Inject
    public ApplicationsServiceImpl(
            @ComponentImport final ApplicationManager applicationManager,
            @ComponentImport final DefaultGroupMembershipService defaultGroupMembershipService,
            @ComponentImport final DirectoryManager directoryManager) {

        this.applicationManager = applicationManager;
        this.defaultGroupMembershipService = defaultGroupMembershipService;
        this.directoryManager = directoryManager;
    }

    @Override
    public ApplicationsBean getApplications() {
        return new ApplicationsBean(applicationManager.findAll().stream()
                .map(application -> ApplicationBeanUtil.toApplicationBean(application, defaultGroupMembershipService))
                .collect(Collectors.toList()));
    }

    @Override
    public ApplicationBean getApplication(
            final long id) {

        try {
            return ApplicationBeanUtil.toApplicationBean(applicationManager.findById(id), defaultGroupMembershipService);
        } catch (ApplicationNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public ApplicationsBean setApplications(
            final ApplicationsBean applicationsBean) {

        final List<ApplicationBean> applicationBeans = new ArrayList<>();

        for (ApplicationBean applicationBean : applicationsBean.getApplications()) {
            try {
                final Application application = applicationManager.findByName(applicationBean.getName());
                applicationBeans.add(setApplication(application.getId(), applicationBean));
            } catch (ApplicationNotFoundException ignored) {
                applicationBeans.add(addApplication(applicationBean));
            }
        }

        return new ApplicationsBean(applicationBeans);
    }

    @Override
    public ApplicationBean addApplication(
            final ApplicationBean applicationBean) {

        try {
            final Application createdApplication = applicationManager.add(ApplicationBeanUtil.toApplication(applicationBean));
            persistApplicationDirectoryMappings(createdApplication, applicationBean);
            persistApplicationBeanAuthenticationGroups(createdApplication, applicationBean);
            persistApplicationBeanAutoAssignmentGroups(createdApplication, applicationBean);
            return getApplication(createdApplication.getId());
        } catch (InvalidCredentialException | ApplicationAlreadyExistsException e) {
            throw new BadRequestException(e);
        }
    }

    @Override
    public ApplicationBean setApplication(
            final long id,
            final ApplicationBean applicationBean) {

        try {
            final Application existingApplication = applicationManager.findById(id);
            final Application modifiedApplication = ApplicationBeanUtil.toApplication(applicationBean, existingApplication);
            final Application updatedApplication = applicationManager.update(modifiedApplication);
            persistApplicationDirectoryMappings(updatedApplication, applicationBean);
            persistApplicationBeanAuthenticationGroups(updatedApplication, applicationBean);
            persistApplicationBeanAutoAssignmentGroups(updatedApplication, applicationBean);
            return getApplication(updatedApplication.getId());
        } catch (ApplicationNotFoundException e) {
            throw new NotFoundException(e);
        } catch (ApplicationManagerException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void deleteApplications(
            final boolean force) {

        if (!force) {
            throw new BadRequestException("Set 'force' query param to true in order to delete all applications");
        }

        applicationManager.findAll().stream()
                .filter(a -> !a.isPermanent())
                .forEach(a -> deleteApplication((a.getId())));
    }

    @Override
    public void deleteApplication(
            final long id) {

        try {
            final Application application = applicationManager.findById(id);
            applicationManager.remove(application);
        } catch (ApplicationNotFoundException e) {
            throw new NotFoundException(e);
        } catch (ApplicationManagerException e) {
            throw new InternalServerErrorException(e);
        }
    }

    void persistApplicationDirectoryMappings(
            final Application application,
            final ApplicationBean applicationBean) {

        if (applicationBean.getDirectoryMappings() == null) {
            return;
        }

        final Map<String, ApplicationDirectoryMapping> applicationDirectoryMappingsByDirectoryName = application.getApplicationDirectoryMappings().stream()
                .collect(Collectors.toMap(adm -> adm.getDirectory().getName(), Function.identity()));
        final Map<String, ApplicationBean.ApplicationDirectoryMapping> applicationBeanDirectoryMappingsByDirectoryName = applicationBean.getDirectoryMappings().stream()
                .collect(Collectors.toMap(ApplicationBean.ApplicationDirectoryMapping::getDirectoryName, Function.identity()));

        final List<ApplicationDirectoryMapping> applicationDirectoryMappingsToCreate = new ArrayList<>();
        final List<ApplicationDirectoryMapping> applicationDirectoryMappingsToUpdate = new ArrayList<>();

        // before performing any actual changes using the application manager, we first collect all data
        // and wait for any potential validation errors (performed in the `toApplicationDirectoryMapping` method)
        for (ApplicationDirectoryMapping applicationDirectoryMapping : toApplicationDirectoryMappings(applicationBeanDirectoryMappingsByDirectoryName.values())) {
            if (!applicationDirectoryMappingsByDirectoryName.containsKey(applicationDirectoryMapping.getDirectory().getName())) {
                applicationDirectoryMappingsToCreate.add(applicationDirectoryMapping);
            } else {
                applicationDirectoryMappingsToUpdate.add(applicationDirectoryMapping);
            }
        }

        for (ApplicationDirectoryMapping applicationDirectoryMapping : applicationDirectoryMappingsToCreate) {
            addApplicationDirectoryMapping(application, applicationDirectoryMapping);
        }

        for (ApplicationDirectoryMapping applicationDirectoryMapping : applicationDirectoryMappingsToUpdate) {
            updateApplicationDirectoryMapping(application, applicationDirectoryMapping);
        }

        for (ApplicationDirectoryMapping applicationDirectoryMapping : applicationDirectoryMappingsByDirectoryName.values()) {
            if (!applicationBeanDirectoryMappingsByDirectoryName.containsKey(applicationDirectoryMapping.getDirectory().getName())) {
                removeApplicationDirectoryMapping(application, applicationDirectoryMapping);
            }
        }
    }

    void persistApplicationBeanAuthenticationGroups(
            final Application application,
            final ApplicationBean applicationBean) {

        if (applicationBean.getDirectoryMappings() == null) {
            return;
        }

        final Map<String, Collection<String>> authenticationGroupsByDirectoryName = applicationBean.getDirectoryMappings().stream()
                .collect(Collectors.toMap(ApplicationBean.ApplicationDirectoryMapping::getDirectoryName, ApplicationBean.ApplicationDirectoryMapping::getAuthenticationGroups));

        for (ApplicationDirectoryMapping applicationDirectoryMapping : application.getApplicationDirectoryMappings()) {
            final Set<String> authenticationGroups = new HashSet<>(authenticationGroupsByDirectoryName.getOrDefault(
                    applicationDirectoryMapping.getDirectory().getName(), Collections.emptySet()));

            final Set<String> currentAuthenticationGroups = applicationDirectoryMapping.getAuthorisedGroupNames();

            final Set<String> authenticationGroupsToAdd = new HashSet<>(authenticationGroups);
            authenticationGroupsToAdd.removeAll(currentAuthenticationGroups);
            addAuthenticationGroups(application, applicationDirectoryMapping.getDirectory(), authenticationGroupsToAdd);

            final Set<String> authenticationGroupsToRemove = new HashSet<>(currentAuthenticationGroups);
            authenticationGroupsToRemove.removeAll(authenticationGroups);
            removeAuthenticationGroups(application, applicationDirectoryMapping.getDirectory(), authenticationGroupsToRemove);
        }
    }

    void persistApplicationBeanAutoAssignmentGroups(
            final Application application,
            final ApplicationBean applicationBean) {

        if (applicationBean.getDirectoryMappings() == null) {
            return;
        }

        final Map<String, Collection<String>> autoAssignmentGroupsByDirectoryName = applicationBean.getDirectoryMappings().stream()
                .collect(Collectors.toMap(ApplicationBean.ApplicationDirectoryMapping::getDirectoryName, ApplicationBean.ApplicationDirectoryMapping::getAutoAssignmentGroups));

        for (ApplicationDirectoryMapping applicationDirectoryMapping : application.getApplicationDirectoryMappings()) {
            final Collection<String> autoAssignmentGroups = autoAssignmentGroupsByDirectoryName.getOrDefault(
                    applicationDirectoryMapping.getDirectory().getName(), Collections.emptyList());

            final Set<String> currentAutoAssignmentGroups;
            try {
                currentAutoAssignmentGroups = new HashSet<>(defaultGroupMembershipService.listAll(application, applicationDirectoryMapping));
            } catch (OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }

            final Set<String> autoAssignmentGroupSet = new HashSet<>(autoAssignmentGroups);

            final Set<String> autoAssignmentGroupsToAdd = new HashSet<>(autoAssignmentGroupSet);
            autoAssignmentGroupsToAdd.removeAll(currentAutoAssignmentGroups);
            addAutoAssignmentGroups(application, applicationDirectoryMapping, autoAssignmentGroupsToAdd, defaultGroupMembershipService);

            final Set<String> autoAssignmentGroupsToRemove = new HashSet<>(currentAutoAssignmentGroups);
            autoAssignmentGroupsToRemove.removeAll(autoAssignmentGroupSet);
            removeAutoAssignmentGroups(application, applicationDirectoryMapping, autoAssignmentGroupsToRemove, defaultGroupMembershipService);
        }
    }

    @Nonnull
    Collection<ApplicationDirectoryMapping> toApplicationDirectoryMappings(
            @Nonnull final Collection<ApplicationBean.ApplicationDirectoryMapping> applicationBeanDirectoryMappings) {

        return applicationBeanDirectoryMappings.stream()
                .map(this::toApplicationDirectoryMapping)
                .collect(Collectors.toList());
    }

    @Nonnull
    ApplicationDirectoryMapping toApplicationDirectoryMapping(
            @Nonnull final ApplicationBean.ApplicationDirectoryMapping applicationBeanDirectoryMapping) {

        if (applicationBeanDirectoryMapping.getDirectoryName() == null || applicationBeanDirectoryMapping.getDirectoryName().isEmpty()) {
            throw new BadRequestException("Application directory mapping must contain a directory name");
        }

        final ImmutableApplicationDirectoryMapping.Builder applicationDirectoryMappingBuilder = ImmutableApplicationDirectoryMapping.builder();
        applicationDirectoryMappingBuilder.setDirectory(findDirectory(applicationBeanDirectoryMapping.getDirectoryName(), directoryManager));

        if (applicationBeanDirectoryMapping.getAuthenticationAllowAll() != null) {
            final boolean authenticationAllowAll = applicationBeanDirectoryMapping.getAuthenticationAllowAll();
            applicationDirectoryMappingBuilder.setAllowAllToAuthenticate(authenticationAllowAll);

            // don't require to set authentication groups if all users are allowed to authenticate
            if (authenticationAllowAll) {
                applicationDirectoryMappingBuilder.setAuthorisedGroupNames(Collections.emptySet());
            }
        }

        // even if all users are allowed to authenticate, it does not hurt to set (ignored) authentication groups if they got passed
        if (applicationBeanDirectoryMapping.getAuthenticationGroups() != null) {
            applicationDirectoryMappingBuilder.setAuthorisedGroupNames(new HashSet<>(applicationBeanDirectoryMapping.getAuthenticationGroups()));
        }

        // the auto assignment groups must be set after these mappings have been persisted...

        if (applicationBeanDirectoryMapping.getAllowedOperations() != null) {
            applicationDirectoryMappingBuilder.setAllowedOperations(new HashSet<>(applicationBeanDirectoryMapping.getAllowedOperations()));
        }

        return applicationDirectoryMappingBuilder.build();
    }

    void addApplicationDirectoryMapping(
            final Application application,
            final ApplicationDirectoryMapping applicationDirectoryMapping) {

        try {
            applicationManager.addDirectoryMapping(
                    application,
                    applicationDirectoryMapping.getDirectory(),
                    applicationDirectoryMapping.isAllowAllToAuthenticate(),
                    applicationDirectoryMapping.getAllowedOperations().toArray(new OperationType[0]));
        } catch (ApplicationNotFoundException | DirectoryNotFoundException e) {
            // both exceptions should not happy anymore at this stage, so handle as internal server error
            throw new InternalServerErrorException(e);
        }
    }

    void updateApplicationDirectoryMapping(
            final Application application,
            final ApplicationDirectoryMapping applicationDirectoryMapping) {

        try {
            applicationManager.updateDirectoryMapping(
                    application,
                    applicationDirectoryMapping.getDirectory(),
                    applicationDirectoryMapping.isAllowAllToAuthenticate(),
                    applicationDirectoryMapping.getAllowedOperations());
        } catch (ApplicationNotFoundException | DirectoryNotFoundException e) {
            // both exceptions should not happy anymore at this stage, so handle as internal server error
            throw new InternalServerErrorException(e);
        }
    }

    void removeApplicationDirectoryMapping(
            final Application application,
            final ApplicationDirectoryMapping applicationDirectoryMapping) {

        try {
            applicationManager.removeDirectoryFromApplication(applicationDirectoryMapping.getDirectory(), application);
        } catch (ApplicationManagerException e) {
            throw new InternalServerErrorException(e);
        }
    }

    void addAuthenticationGroups(
            final Application application,
            final Directory directory,
            final Collection<String> authenticationGroupsToAdd) {

        for (String authenticationGroupToAdd : authenticationGroupsToAdd) {
            try {
                applicationManager.addGroupMapping(application, directory, authenticationGroupToAdd);
            } catch (ApplicationNotFoundException e) {
                throw new InternalServerErrorException(e);
            }
        }
    }

    void removeAuthenticationGroups(
            final Application application,
            final Directory directory,
            final Collection<String> authenticationGroupsToRemove) {

        for (String authenticationGroupToRemove : authenticationGroupsToRemove) {
            try {
                applicationManager.removeGroupMapping(application, directory, authenticationGroupToRemove);
            } catch (ApplicationNotFoundException e) {
                throw new InternalServerErrorException(e);
            }
        }
    }

    void addAutoAssignmentGroups(
            final Application application,
            final ApplicationDirectoryMapping applicationDirectoryMapping,
            final Collection<String> autoAssignmentGroupsToAdd,
            final DefaultGroupMembershipService defaultGroupMembershipService) {

        for (String autoAssignmentGroupToAdd : autoAssignmentGroupsToAdd) {
            try {
                defaultGroupMembershipService.add(application, applicationDirectoryMapping, autoAssignmentGroupToAdd);
            } catch (OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }
        }
    }

    void removeAutoAssignmentGroups(
            final Application application,
            final ApplicationDirectoryMapping applicationDirectoryMapping,
            final Collection<String> autoAssignmentGroupsToRemove,
            final DefaultGroupMembershipService defaultGroupMembershipService) {

        for (String autoAssignmentGroupToRemove : autoAssignmentGroupsToRemove) {
            try {
                defaultGroupMembershipService.remove(application, applicationDirectoryMapping, autoAssignmentGroupToRemove);
            } catch (OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }
        }
    }

    @Nonnull
    private static Directory findDirectory(
            final String directoryName,
            final DirectoryManager directoryManager) {

        try {
            return directoryManager.findDirectoryByName(directoryName);
        } catch (DirectoryNotFoundException e) {
            throw new BadRequestException(e);
        }
    }

}

package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.exception.DirectoryInstantiationException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.search.EntityDescriptor;
import com.atlassian.crowd.search.builder.QueryBuilder;
import com.atlassian.crowd.search.query.entity.EntityQuery;
import com.deftdevs.bootstrapi.commons.exception.DirectoryNotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.service.AbstractDirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryModelUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.atlassian.crowd.embedded.api.DirectoryType.INTERNAL;

public class DirectoriesServiceImpl extends AbstractDirectoriesService {

    private static final int RETRY_AFTER_IN_SECONDS = 5;

    private final DirectoryManager directoryManager;
    private final GroupsService groupsService;
    private final UsersService usersService;

    public DirectoriesServiceImpl(
            final DirectoryManager directoryManager,
            final GroupsService groupsService,
            final UsersService usersService) {

        this.directoryManager = directoryManager;
        this.groupsService = groupsService;
        this.usersService = usersService;
    }

    @Override
    public Map<String, ? extends AbstractDirectoryModel> getDirectories() {
        return findAllDirectories().stream()
                .map(DirectoryModelUtil::toDirectoryModel)
                .collect(Collectors.toMap(AbstractDirectoryModel::getName, Function.identity()));
    }

    @Override
    public AbstractDirectoryModel getDirectory(
            final long id) {

        final Directory directory = findDirectory(id);
        return DirectoryModelUtil.toDirectoryModel(directory);
    }

    @Override
    public AbstractDirectoryModel setDirectory(
            final long id,
            final AbstractDirectoryModel directoryModel) {

        final Directory existingDirectory = findDirectory(id);
        final AbstractDirectoryModel resultDirectoryModel;

        try {
            final Directory mergedDirectory = DirectoryModelUtil.toDirectory(directoryModel, existingDirectory);
            final Directory updatedDirectory = directoryManager.updateDirectory(mergedDirectory);
            resultDirectoryModel = DirectoryModelUtil.toDirectoryModel(updatedDirectory);
        } catch (DirectoryModelUtil.UnsupportedDirectoryModelException e) {
            throw new BadRequestException(String.format(
                    "Setting directory type '%s' is not supported (yet)", e.getMessage()));
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException e) {
            // this should not happen
            throw new InternalServerErrorException(String.format(
                    "When trying to update directory '%s', it could not be found anymore", existingDirectory.getName()));
        }

        handleGroupsAndUsers(directoryModel, resultDirectoryModel);

        return resultDirectoryModel;
    }

    @Override
    public AbstractDirectoryModel addDirectory(
            final AbstractDirectoryModel directoryModel) {

        final AbstractDirectoryModel resultDirectoryModel;

        try {
            final Directory directory = DirectoryModelUtil.toDirectory(directoryModel);
            final Directory addedDirectory = directoryManager.addDirectory(directory);
            resultDirectoryModel = DirectoryModelUtil.toDirectoryModel(addedDirectory);
        } catch (DirectoryModelUtil.UnsupportedDirectoryModelException e) {
            throw new BadRequestException(String.format(
                    "Adding directory type '%s' is not supported (yet)", e.getMessage()));
        } catch (DirectoryInstantiationException e) {
            throw new InternalServerErrorException(String.format("Could not create directory '%s'", directoryModel.getName()));
        }

        handleGroupsAndUsers(directoryModel, resultDirectoryModel);

        return resultDirectoryModel;
    }

    @Override
    public void deleteDirectories(
            final boolean force) {

        if (!force) {
            throw new BadRequestException("'force = true' must be supplied to delete all entries");
        }

        final List<Directory> directories = findAllDirectories();

        directories.stream()
                .sorted(new DirectoryComparator())
                .limit(directories.size() - 1L)
                .forEach(d -> deleteDirectory(d.getId()));
    }

    @Override
    public void deleteDirectory(
            final long id) {

        if (findAllDirectories().size() <= 1) {
            throw new BadRequestException("Cannot delete directory '%s' as this is the last remaining directory");
        }

        final Directory directory = findDirectory(id);

        try {
            directoryManager.removeDirectory(directory);
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException e) {
            throw new InternalServerErrorException(String.format(
                    "When trying to delete directory '%s', it could not be found anymore", directory.getName()));
        } catch (DirectoryCurrentlySynchronisingException e) {
            throw new ServiceUnavailableException(e, RETRY_AFTER_IN_SECONDS);
        }
    }

    @Override
    protected Set<Class<? extends AbstractDirectoryModel>> getSupportedClassesForUpdate() {
        return DirectoryModelUtil.SUPPORTED_DIRECTORY_BEAN_TYPES;
    }

    @Nonnull
    Directory findDirectory(
            final long id) {

        try {
            return directoryManager.findDirectoryById(id);
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException e) {
            throw new DirectoryNotFoundException(id);
        }
    }

    @Nonnull
    List<Directory> findAllDirectories() {
        final EntityQuery<Directory> allDirectoriesEntityQuery = QueryBuilder
                .queryFor(Directory.class, EntityDescriptor.directory())
                .returningAtMost(EntityQuery.ALL_RESULTS);

        return directoryManager.searchDirectories(allDirectoriesEntityQuery);
    }

    private void handleGroupsAndUsers(AbstractDirectoryModel directoryModel, AbstractDirectoryModel resultDirectoryModel) {
        if (DirectoryInternalModel.class.equals(directoryModel.getClass()) && directoryModel.getClass().equals(resultDirectoryModel.getClass())) {
            final DirectoryInternalModel directoryInternalModel = (DirectoryInternalModel) directoryModel;
            final DirectoryInternalModel resultDirectoryInternalModel = (DirectoryInternalModel) resultDirectoryModel;

            if (directoryInternalModel.getGroups() != null) {
                final Map<String, GroupModel> resultGroupModels = groupsService.setGroups(resultDirectoryInternalModel.getId(), directoryInternalModel.getGroups());
                resultDirectoryInternalModel.setGroups(resultGroupModels);
            }

            if (directoryInternalModel.getUsers() != null) {
                final Map<String, UserModel> resultUserModels = usersService.setUsers(resultDirectoryInternalModel.getId(), directoryInternalModel.getUsers());
                resultDirectoryInternalModel.setUsers(resultUserModels);
            }
        }
    }

    /**
     * This comparator allows sorting directories as follows:
     * - Non-internal directories come first
     * - For the same directory types, directories with more recent creation date come first
     * This comparator is used for the "delete-all" method in order to make sure that the oldest internal directory
     * will never be deleted in order not to lock out from Crowd.
     */
    static class DirectoryComparator implements Comparator<Directory> {
        @Override
        public int compare(Directory d1, Directory d2) {
            if (d1.getType().equals(INTERNAL) && !d2.getType().equals(INTERNAL)) {
                return 1;
            }
            if (!d1.getType().equals(INTERNAL) && d2.getType().equals(INTERNAL)) {
                return -1;
            }
            return d2.getCreatedDate().compareTo(d1.getCreatedDate());
        }
    }

}

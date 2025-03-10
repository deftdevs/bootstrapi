package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.exception.DirectoryInstantiationException;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.search.EntityDescriptor;
import com.atlassian.crowd.search.builder.QueryBuilder;
import com.atlassian.crowd.search.query.entity.EntityQuery;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalBean;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.exception.NotFoundExceptionForDirectory;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryBeanUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.atlassian.crowd.embedded.api.DirectoryType.INTERNAL;

@Component
@ExportAsService(DirectoriesService.class)
public class DirectoriesServiceImpl implements DirectoriesService {

    private static final int RETRY_AFTER_IN_SECONDS = 5;

    @ComponentImport
    private final DirectoryManager directoryManager;

    private final GroupsService groupsService;

    private final UsersService usersService;

    @Inject
    public DirectoriesServiceImpl(
            final DirectoryManager directoryManager,
            final GroupsService groupsService,
            final UsersService usersService) {

        this.directoryManager = directoryManager;
        this.groupsService = groupsService;
        this.usersService = usersService;
    }

    @Override
    public List<AbstractDirectoryBean> getDirectories() {
        return findAllDirectories().stream()
                .map(DirectoryBeanUtil::toDirectoryBean)
                .collect(Collectors.toList());
    }

    @Override
    public AbstractDirectoryBean getDirectory(
            final long id) {

        final Directory directory = findDirectory(id);
        return DirectoryBeanUtil.toDirectoryBean(directory);
    }

    @Override
    public List<AbstractDirectoryBean> setDirectories(
            @NotNull final List<AbstractDirectoryBean> directoryBeans,
            final boolean testConnection) {

        final Map<String, Directory> existingDirectoriesByName = findAllDirectories().stream()
                .collect(Collectors.toMap(Directory::getName, Function.identity()));

        final List<AbstractDirectoryBean> resultDirectories = new ArrayList<>();

        for (AbstractDirectoryBean directoryBean : directoryBeans) {
            if (existingDirectoriesByName.containsKey(directoryBean.getName())) {
                resultDirectories.add(setDirectory(existingDirectoriesByName.get(directoryBean.getName()).getId(), directoryBean, testConnection));
            } else {
                resultDirectories.add(addDirectory(directoryBean, testConnection));
            }
        }

        return resultDirectories;
    }

    @Override
    public AbstractDirectoryBean setDirectory(
            final long id,
            @NotNull final AbstractDirectoryBean directoryBean,
            final boolean testConnection) {

        final Directory existingDirectory = findDirectory(id);
        final AbstractDirectoryBean resultDirectoryBean;

        try {
            final Directory mergedDirectory = DirectoryBeanUtil.toDirectory(directoryBean, existingDirectory);
            final Directory updatedDirectory = directoryManager.updateDirectory(mergedDirectory);
            resultDirectoryBean = DirectoryBeanUtil.toDirectoryBean(updatedDirectory);
        } catch (DirectoryBeanUtil.UnsupportedDirectoryBeanException e) {
            throw new BadRequestException(String.format(
                    "Setting directory type '%s' is not supported (yet)", e.getMessage()));
        } catch (DirectoryNotFoundException e) {
            // this should not happen
            throw new InternalServerErrorException(String.format(
                    "When trying to update directory '%s', it could not be found anymore", existingDirectory.getName()));
        }

        handleGroupsAndUsers(directoryBean, resultDirectoryBean);

        return resultDirectoryBean;
    }

    @Override
    public AbstractDirectoryBean addDirectory(
            final @NotNull AbstractDirectoryBean directoryBean,
            final boolean testConnection) {

        final AbstractDirectoryBean resultDirectoryBean;

        try {
            final Directory directory = DirectoryBeanUtil.toDirectory(directoryBean);
            final Directory addedDirectory = directoryManager.addDirectory(directory);
            resultDirectoryBean = DirectoryBeanUtil.toDirectoryBean(addedDirectory);
        } catch (DirectoryBeanUtil.UnsupportedDirectoryBeanException e) {
            throw new BadRequestException(String.format(
                    "Adding directory type '%s' is not supported (yet)", e.getMessage()));
        } catch (DirectoryInstantiationException e) {
            throw new InternalServerErrorException(String.format("Could not create directory '%s'", directoryBean.getName()));
        }

        handleGroupsAndUsers(directoryBean, resultDirectoryBean);

        return resultDirectoryBean;
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
        } catch (DirectoryNotFoundException e) {
            throw new InternalServerErrorException(String.format(
                    "When trying to delete directory '%s', it could not be found anymore", directory.getName()));
        } catch (DirectoryCurrentlySynchronisingException e) {
            throw new ServiceUnavailableException(e, RETRY_AFTER_IN_SECONDS);
        }
    }

    @Nonnull
    Directory findDirectory(
            final long id) {

        try {
            return directoryManager.findDirectoryById(id);
        } catch (DirectoryNotFoundException e) {
            throw new NotFoundExceptionForDirectory(id);
        }
    }

    @Nonnull
    List<Directory> findAllDirectories() {
        final EntityQuery<Directory> allDirectoriesEntityQuery = QueryBuilder
                .queryFor(Directory.class, EntityDescriptor.directory())
                .returningAtMost(EntityQuery.ALL_RESULTS);

        return directoryManager.searchDirectories(allDirectoriesEntityQuery);
    }

    private void handleGroupsAndUsers(AbstractDirectoryBean directoryBean, AbstractDirectoryBean resultDirectoryBean) {
        if (DirectoryInternalBean.class.equals(directoryBean.getClass()) && directoryBean.getClass().equals(resultDirectoryBean.getClass())) {
            final DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) directoryBean;
            final DirectoryInternalBean resultDirectoryInternalBean = (DirectoryInternalBean) resultDirectoryBean;

            if (directoryInternalBean.getGroups() != null) {
                // this is the new implementation using a groups bean
                final List<GroupBean> resultGroupBeans = groupsService.setGroups(resultDirectoryInternalBean.getId(), directoryInternalBean.getGroups());
                resultDirectoryInternalBean.setGroups(resultGroupBeans);
            }

            if (directoryInternalBean.getUsers() != null) {
                // this is the old implementation using a list of users
                final List<UserBean> resultUserBeans = usersService.setUsers(resultDirectoryInternalBean.getId(), directoryInternalBean.getUsers());
                resultDirectoryInternalBean.setUsers(resultUserBeans);
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

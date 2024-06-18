package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.InvalidGroupException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.ReadOnlyGroupException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupTemplate;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.crowd.exception.NotFoundExceptionForDirectory;
import com.deftdevs.bootstrapi.crowd.exception.NotFoundExceptionForGroup;
import com.deftdevs.bootstrapi.crowd.model.GroupsBean;
import com.deftdevs.bootstrapi.crowd.model.util.GroupBeanUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@ExportAsService(GroupsService.class)
public class GroupsServiceImpl implements GroupsService {

    @ComponentImport
    private final DirectoryManager directoryManager;

    @Inject
    public GroupsServiceImpl(
            final DirectoryManager directoryManager) {

        this.directoryManager = directoryManager;
    }

    @Override
    public GroupBean getGroup(
            final long directoryId,
            final String groupName) {

        final Group group = findGroup(directoryId, groupName);

        if (group == null) {
            throw new NotFoundExceptionForGroup(groupName);
        }

        return GroupBeanUtil.toGroupBean(group);
    }

    @Override
    public GroupBean createGroup(
            final long directoryId,
            final GroupBean groupBean) {

        if (groupBean.getName() == null) {
            throw new BadRequestException("Cannot create group, group name is required");
        }

        final Group existingGroup = findGroup(directoryId, groupBean.getName());

        if (existingGroup != null) {
            throw new BadRequestException(String.format("Group '%s' already exists", groupBean.getName()));
        }

        final GroupTemplate groupTemplate = new GroupTemplate(groupBean.getName(), directoryId);
        groupTemplate.setDescription(groupBean.getDescription());
        groupTemplate.setActive(groupBean.getActive() == null || groupBean.getActive());

        try {
            return GroupBeanUtil.toGroupBean(directoryManager.addGroup(directoryId, groupTemplate));
        } catch (DirectoryPermissionException | InvalidGroupException e) {
            // A permission exception should only happen if we try adding the group
            // a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | OperationFailedException e) {
            // At this point, we know the group exists, thus directory not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public GroupBean updateGroup(
            final long directoryId,
            final String groupName,
            final GroupBean groupBean) {

        Group group = findGroup(directoryId, groupName);

        if (group == null) {
            throw new NotFoundExceptionForGroup(groupName);
        }

        if (groupBean.getName() != null && !groupBean.getName().equals(group.getName())) {
            group = renameGroup(directoryId, groupName, groupBean.getName());
        }

        if (groupBean.getDescription() != null || groupBean.getActive() != null) {
            group = updateGroup(directoryId, createGroupTemplate(group, groupBean));
        }

        return GroupBeanUtil.toGroupBean(group);
    }

    @Override
    public GroupBean setGroup(
            final long directoryId,
            final String groupName,
            final GroupBean groupBean) {

        final Group group = findGroup(directoryId, groupName);

        if (group == null) {
            return createGroup(directoryId, groupBean);
        }

        return updateGroup(directoryId, groupName, groupBean);
    }

    @Override
    public GroupsBean setGroups(
            final long directoryId,
            final GroupsBean groupsBean) {

        if (groupsBean == null || groupsBean.getGroups() == null) {
            return new GroupsBean(Collections.emptyList());
        }

        return new GroupsBean(groupsBean.getGroups().stream()
                .map(groupBean -> setGroup(directoryId, groupBean.getName(), groupBean))
                .collect(Collectors.toList()));
    }

    @Nullable
    Group findGroup(
            final long directoryId,
            final String groupName) {

        try {
            return directoryManager.findGroupByName(directoryId, groupName);
        } catch (DirectoryNotFoundException e) {
            throw new NotFoundExceptionForDirectory(directoryId);
        } catch (GroupNotFoundException e) {
            // Ignore, will be handled differently
        } catch (OperationFailedException e) {
            throw new InternalServerErrorException(e);
        }

        return null;
    }

    Group updateGroup(
            final long directoryId,
            final GroupTemplate groupTemplate) {

        try {
            return directoryManager.updateGroup(directoryId, groupTemplate);
        } catch (DirectoryPermissionException | InvalidGroupException | ReadOnlyGroupException e) {
            // A permission exception should only happen if we try updating the group
            // a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | GroupNotFoundException | OperationFailedException e) {
            // At this point, we know the group exists, thus directory or group not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    Group renameGroup(
            final long directoryId,
            final String currentGroupName,
            final String newGroupName) {

        try {
            return directoryManager.renameGroup(directoryId, currentGroupName, newGroupName);
        } catch (DirectoryPermissionException | InvalidGroupException e) {
            // A permission exception should only happen if we try change the name
            // of a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | GroupNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    GroupTemplate createGroupTemplate(
            final Group group,
            final GroupBean groupBean) {

        final GroupTemplate groupTemplate = new GroupTemplate(group);

        if (groupBean.getDescription() != null) {
            groupTemplate.setDescription(groupBean.getDescription());
        }
        if (groupBean.getActive() != null) {
            groupTemplate.setActive(groupBean.getActive());
        }

        return groupTemplate;
    }

}

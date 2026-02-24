package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.exception.InvalidGroupException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.ReadOnlyGroupException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupTemplate;
import com.deftdevs.bootstrapi.commons.exception.DirectoryNotFoundException;
import com.deftdevs.bootstrapi.commons.exception.GroupNotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.crowd.model.util.GroupModelUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class GroupsServiceImpl implements GroupsService {

    private final DirectoryManager directoryManager;

    public GroupsServiceImpl(
            final DirectoryManager directoryManager) {

        this.directoryManager = directoryManager;
    }

    @Override
    public GroupModel getGroup(
            final long directoryId,
            final String groupName) {

        final Group group = findGroup(directoryId, groupName);

        if (group == null) {
            throw new GroupNotFoundException(groupName);
        }

        return GroupModelUtil.toGroupModel(group);
    }

    @Override
    public GroupModel createGroup(
            final long directoryId,
            final GroupModel groupModel) {

        if (groupModel.getName() == null) {
            throw new BadRequestException("Cannot create group, group name is required");
        }

        final Group existingGroup = findGroup(directoryId, groupModel.getName());

        if (existingGroup != null) {
            throw new BadRequestException(String.format("Group '%s' already exists", groupModel.getName()));
        }

        final GroupTemplate groupTemplate = new GroupTemplate(groupModel.getName(), directoryId);
        groupTemplate.setDescription(groupModel.getDescription());
        groupTemplate.setActive(groupModel.getActive() == null || groupModel.getActive());

        try {
            return GroupModelUtil.toGroupModel(directoryManager.addGroup(directoryId, groupTemplate));
        } catch (DirectoryPermissionException | InvalidGroupException e) {
            // A permission exception should only happen if we try adding the group
            // a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException | OperationFailedException e) {
            // At this point, we know the group exists, thus directory not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public GroupModel updateGroup(
            final long directoryId,
            final String groupName,
            final GroupModel groupModel) {

        Group group = findGroup(directoryId, groupName);

        if (group == null) {
            throw new GroupNotFoundException(groupName);
        }

        if (groupModel.getName() != null && !groupModel.getName().equals(group.getName())) {
            group = renameGroup(directoryId, groupName, groupModel.getName());
        }

        if (groupModel.getDescription() != null || groupModel.getActive() != null) {
            group = updateGroup(directoryId, createGroupTemplate(group, groupModel));
        }

        return GroupModelUtil.toGroupModel(group);
    }

    @Override
    public GroupModel setGroup(
            final long directoryId,
            final String groupName,
            final GroupModel groupModel) {

        final Group group = findGroup(directoryId, groupName);

        if (group == null) {
            return createGroup(directoryId, groupModel);
        }

        return updateGroup(directoryId, groupName, groupModel);
    }

    @Override
    public Map<String, GroupModel> setGroups(
            final long directoryId,
            final Map<String, GroupModel> groupModels) {

        if (groupModels == null) {
            return Collections.emptyMap();
        }

        final Map<String, GroupModel> resultGroupModels = new LinkedHashMap<>();
        for (Map.Entry<String, GroupModel> entry : groupModels.entrySet()) {
            final GroupModel resultGroupModel = setGroup(directoryId, entry.getKey(), entry.getValue());
            resultGroupModels.put(resultGroupModel.getName(), resultGroupModel);
        }
        return resultGroupModels;
    }

    @Nullable
    Group findGroup(
            final long directoryId,
            final String groupName) {

        try {
            return directoryManager.findGroupByName(directoryId, groupName);
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException e) {
            throw new DirectoryNotFoundException(directoryId);
        } catch (com.atlassian.crowd.exception.GroupNotFoundException e) {
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
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                 com.atlassian.crowd.exception.GroupNotFoundException | OperationFailedException e) {
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
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                 com.atlassian.crowd.exception.GroupNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    GroupTemplate createGroupTemplate(
            final Group group,
            final GroupModel groupModel) {

        final GroupTemplate groupTemplate = new GroupTemplate(group);

        if (groupModel.getDescription() != null) {
            groupTemplate.setDescription(groupModel.getDescription());
        }
        if (groupModel.getActive() != null) {
            groupTemplate.setActive(groupModel.getActive());
        }

        return groupTemplate;
    }

}

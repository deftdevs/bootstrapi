package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.crowd.rest.api.GroupResource;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@SystemAdminOnly
@Path(BootstrAPI.GROUP)
public class GroupResourceImpl implements GroupResource {

    private final GroupsService groupsService;

    public GroupResourceImpl(
            final GroupsService groupsService) {

        this.groupsService = groupsService;
    }

    @Override
    public Response getGroup(
            final long directoryId,
            final String groupName) {

        final GroupModel groupModel = groupsService.getGroup(directoryId, groupName);
        return Response.ok(groupModel).build();
    }

    @Override
    public Response createGroup(
            final long directoryId,
            final GroupModel groupModel) {

        final GroupModel resultGroupModel = groupsService.createGroup(directoryId, groupModel);
        return Response.ok(resultGroupModel).build();
    }

    @Override
    public Response updateGroup(
            final long directoryId,
            final String groupName,
            final GroupModel groupModel) {

        final GroupModel resultGroupModel = groupsService.updateGroup(directoryId, groupName, groupModel);
        return Response.ok(resultGroupModel).build();
    }

}

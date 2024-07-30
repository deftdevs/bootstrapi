package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.crowd.rest.api.GroupResource;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@SystemAdminOnly
@Path(BootstrAPI.GROUP)
public class GroupResourceImpl implements GroupResource {

    private final GroupsService groupsService;

    @Inject
    public GroupResourceImpl(
            final GroupsService groupsService) {

        this.groupsService = groupsService;
    }

    @Override
    public Response getGroup(
            final long directoryId,
            final String groupName) {

        final GroupBean groupBean = groupsService.getGroup(directoryId, groupName);
        return Response.ok(groupBean).build();
    }

    @Override
    public Response createGroup(
            final long directoryId,
            final GroupBean groupBean) {

        final GroupBean resultGroupBean = groupsService.createGroup(directoryId, groupBean);
        return Response.ok(resultGroupBean).build();
    }

    @Override
    public Response updateGroup(
            final long directoryId,
            final String groupName,
            final GroupBean groupBean) {

        final GroupBean resultGroupBean = groupsService.updateGroup(directoryId, groupName, groupBean);
        return Response.ok(resultGroupBean).build();
    }

}

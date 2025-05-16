package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.crowd.rest.api.GroupsResource;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@SystemAdminOnly
@Path(BootstrAPI.GROUPS)
public class GroupsResourceImpl implements GroupsResource {

    private final GroupsService groupsService;

    @Inject
    public GroupsResourceImpl(
            final GroupsService groupsService) {

        this.groupsService = groupsService;
    }

    @Override
    public Response setGroups(
            final long directoryId,
            final List<GroupModel> groupModels) {

        final List<GroupModel> resultGroupModels = groupsService.setGroups(directoryId, groupModels);
        return Response.ok(resultGroupModels).build();
    }

}

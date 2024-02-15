package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.GroupBean;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.GroupsBean;
import de.aservo.confapi.crowd.rest.api.GroupsResource;
import de.aservo.confapi.crowd.service.api.GroupsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(ConfAPI.GROUPS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class GroupsResourceImpl implements GroupsResource {

    private final GroupsService groupsService;

    @Inject
    public GroupsResourceImpl(
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

    @Override
    public Response setGroups(
            final long directoryId,
            final GroupsBean groupBeans) {

        final GroupsBean resultGroupsBean = groupsService.setGroups(directoryId, groupBeans);
        return Response.ok(resultGroupsBean).build();
    }

}

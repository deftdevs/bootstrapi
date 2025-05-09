package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;
import com.deftdevs.bootstrapi.crowd.model._AllBean;
import com.deftdevs.bootstrapi.crowd.model._AllBeanConfigStatus;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ExportAsService(_AllService.class)
public class _AllServiceImpl implements _AllService<_AllBean> {

    private final CrowdSettingsGeneralService settingsService;
    private final UsersService usersService;
    private final GroupsService groupsService;
    private final ApplicationsService applicationsService;

    @Inject
    public _AllServiceImpl(
            final CrowdSettingsGeneralService settingsService,
            final UsersService usersService,
            final GroupsService groupsService,
            final ApplicationsService applicationsService) {

        this.settingsService = settingsService;
        this.usersService = usersService;
        this.groupsService = groupsService;
        this.applicationsService = applicationsService;
    }

    @Override
    public _AllBean setAll(
            final _AllBean allBean) {

        final _AllBean result = new _AllBean();
        final Map<String, _AllBeanConfigStatus> status = new HashMap<>();

        // Apply settings
        if (allBean.getSettings() != null) {
            try {
                result.setSettings(settingsService.setSettingsGeneral(allBean.getSettings()));
                status.put("settings", _AllBeanConfigStatus.success());
            } catch (Exception e) {
                status.put("settings", _AllBeanConfigStatus.error(
                        Response.Status.INTERNAL_SERVER_ERROR,
                        "Failed to apply settings",
                        e.getMessage()
                ));
            }
        }

        // Apply groups (before users, since users may reference groups)
        Map<String, GroupBean> groupsMap = allBean.getGroups();
        if (groupsMap != null && !groupsMap.isEmpty()) {
            try {
                // Validate group identifiers
                for (Map.Entry<String, GroupBean> entry : groupsMap.entrySet()) {
                    String key = entry.getKey();
                    GroupBean group = entry.getValue();
                    if (group.getName() == null) {
                        group.setName(key);
                    } else if (!key.equals(group.getName())) {
                        status.put("groups", _AllBeanConfigStatus.error(
                            Response.Status.BAD_REQUEST,
                            "Group identifier mismatch",
                            String.format("Map key '%s' does not match group name '%s'", key, group.getName())
                        ));
                        continue;
                    }
                }

                if (!status.containsKey("groups")) {
                    List<GroupBean> groupsList = new ArrayList<>(groupsMap.values());
                    List<GroupBean> updatedGroups = groupsService.setGroups(1L, groupsList);

                    Map<String, GroupBean> resultGroups = new HashMap<>();
                    for (GroupBean group : updatedGroups) {
                        resultGroups.put(group.getName(), group);
                    }
                    result.setGroups(resultGroups);
                    status.put("groups", _AllBeanConfigStatus.success());
                }
            } catch (Exception e) {
                status.put("groups", _AllBeanConfigStatus.error(
                        Response.Status.BAD_REQUEST,
                        "Failed to apply groups configuration",
                        e.getMessage()
                ));
            }
        }

        // Apply users
        Map<String, UserBean> usersMap = allBean.getUsers();
        if (usersMap != null && !usersMap.isEmpty()) {
            try {
                // Validate user identifiers
                for (Map.Entry<String, UserBean> entry : usersMap.entrySet()) {
                    String key = entry.getKey();
                    UserBean user = entry.getValue();
                    if (user.getUsername() == null) {
                        user.setUsername(key);
                    } else if (!key.equals(user.getUsername())) {
                        status.put("users", _AllBeanConfigStatus.error(
                            Response.Status.BAD_REQUEST,
                            "User identifier mismatch",
                            String.format("Map key '%s' does not match username '%s'", key, user.getUsername())
                        ));
                        continue;
                    }
                }

                if (!status.containsKey("users")) {
                    List<UserBean> usersList = new ArrayList<>(usersMap.values());
                    List<UserBean> updatedUsers = usersService.setUsers(1L, usersList);

                    Map<String, UserBean> resultUsers = new HashMap<>();
                    for (UserBean user : updatedUsers) {
                        resultUsers.put(user.getUsername(), user);
                    }
                    result.setUsers(resultUsers);
                    status.put("users", _AllBeanConfigStatus.success());
                }
            } catch (Exception e) {
                status.put("users", _AllBeanConfigStatus.error(
                        Response.Status.BAD_REQUEST,
                        "Failed to apply users configuration",
                        e.getMessage()
                ));
            }
        }

        // Apply applications
        Map<String, ApplicationBean> appsMap = allBean.getApplications();
        if (appsMap != null && !appsMap.isEmpty()) {
            try {
                // Validate application identifiers
                for (Map.Entry<String, ApplicationBean> entry : appsMap.entrySet()) {
                    String key = entry.getKey();
                    ApplicationBean app = entry.getValue();
                    if (app.getName() == null) {
                        app.setName(key);
                    } else if (!key.equals(app.getName())) {
                        status.put("applications", _AllBeanConfigStatus.error(
                            Response.Status.BAD_REQUEST,
                            "Application identifier mismatch",
                            String.format("Map key '%s' does not match application name '%s'", key, app.getName())
                        ));
                        continue;
                    }
                }

                if (!status.containsKey("applications")) {
                    List<ApplicationBean> appsList = new ArrayList<>(appsMap.values());
                    List<ApplicationBean> updatedApps = applicationsService.setApplications(appsList);

                    Map<String, ApplicationBean> resultApps = new HashMap<>();
                    for (ApplicationBean app : updatedApps) {
                        resultApps.put(app.getName(), app);
                    }
                    result.setApplications(resultApps);
                    status.put("applications", _AllBeanConfigStatus.success());
                }
            } catch (Exception e) {
                status.put("applications", _AllBeanConfigStatus.error(
                        Response.Status.BAD_REQUEST,
                        "Failed to apply applications configuration",
                        e.getMessage()
                ));
            }
        }

        result.setStatus(status);
        return result;
    }
}

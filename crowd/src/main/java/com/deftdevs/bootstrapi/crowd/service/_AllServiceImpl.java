package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.type._AllBeanStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.crowd.model._AllBean;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@ExportAsService(_AllService.class)
public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllBean> {

    private final CrowdSettingsGeneralService settingsService;
    private final DirectoriesService directoriesService;
    private final ApplicationsService applicationsService;

    @Inject
    public _AllServiceImpl(
            final CrowdSettingsGeneralService settingsService,
            final DirectoriesService directoriesService,
            final ApplicationsService applicationsService) {

        this.settingsService = settingsService;
        this.directoriesService = directoriesService;
        this.applicationsService = applicationsService;
    }

    @Override
    public _AllBean setAll(final _AllBean allBean) {
        final _AllBean result = new _AllBean();
        final Map<String, _AllBeanStatus> status = new HashMap<>();

//        // Handle settings
//        if (allBean.getSettings() != null) {
//            try {
//                result.setSettings(settingsService.setSettingsGeneral(allBean.getSettings()));
//                status.put("settings", _AllBeanStatus.success());
//            } catch (Exception e) {
//                status.put("settings", _AllBeanStatus.error(
//                        Response.Status.INTERNAL_SERVER_ERROR,
//                        "Failed to apply settings",
//                        e.getMessage()
//                ));
//            }
//        }

        setEntity(allBean.getSettings(), settingsService::setSettingsGeneral);

        setEntities(allBean.getDirectories(), AbstractDirectoryBean::getName, directoriesService::setDirectories);

//        // Process entities using a generic handler
//        processEntities(allBean.getGroups(), "groups", GroupBean::getName, GroupsService::setGroups, result::setGroups, status);

//        processEntities(allBean.getUsers(), "users", UserBean::getUsername,
//                users -> usersService.setUsers(1L, users), result::setUsers, status);
//
//        processEntities(allBean.getApplications(), "applications", ApplicationBean::getName,
//                applicationsService::setApplications, result::setApplications, status);

        result.setStatus(status);
        return result;
    }

    private <T> void processEntities(
            Map<String, T> entityMap,
            String entityType,
            Function<T, String> getIdentifier,
            Function<List<T>, List<T>> updateFunction,
            BiFunction<Map<String, T>, _AllBean, _AllBean> resultSetter,
            Map<String, _AllBeanStatus> status) {

        if (entityMap == null || entityMap.isEmpty()) {
            return;
        }

        try {
            // Validate entity identifiers
            for (Map.Entry<String, T> entry : entityMap.entrySet()) {
                String key = entry.getKey();
                T entity = entry.getValue();
                String identifier = getIdentifier.apply(entity);

                if (identifier == null) {
                    // Try to set the key as the identifier using reflection
                    try {
                        entity.getClass().getMethod("set" + entityType.substring(0, 1).toUpperCase() +
                                entityType.substring(1, entityType.length() - 1), String.class)
                                .invoke(entity, key);
                    } catch (Exception e) {
                        // If reflection fails, report the error
                        status.put(entityType, _AllBeanStatus.error(
                            Response.Status.BAD_REQUEST,
                            entityType + " identifier missing",
                            "Could not set identifier for key: " + key
                        ));
                        return;
                    }
                } else if (!key.equals(identifier)) {
                    status.put(entityType, _AllBeanStatus.error(
                        Response.Status.BAD_REQUEST,
                        entityType.substring(0, 1).toUpperCase() + entityType.substring(1) + " identifier mismatch",
                        String.format("Map key '%s' does not match %s '%s'", key, entityType.substring(0, entityType.length() - 1), identifier)
                    ));
                    return;
                }
            }

            if (!status.containsKey(entityType)) {
                List<T> entityList = new ArrayList<>(entityMap.values());
                List<T> updatedEntities = updateFunction.apply(entityList);

                Map<String, T> resultMap = new HashMap<>();
                for (T entity : updatedEntities) {
                    resultMap.put(getIdentifier.apply(entity), entity);
                }
                resultSetter.apply(resultMap, null);
                status.put(entityType, _AllBeanStatus.success());
            }
        } catch (Exception e) {
            status.put(entityType, _AllBeanStatus.error(
                    Response.Status.BAD_REQUEST,
                    "Failed to apply " + entityType + " configuration",
                    e.getMessage()
            ));
        }
    }
}

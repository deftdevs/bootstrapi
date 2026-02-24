package com.deftdevs.bootstrapi.crowd.service;

import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service._AbstractAllServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;

import java.util.HashMap;
import java.util.Map;

public class _AllServiceImpl extends _AbstractAllServiceImpl<_AllModel> {

    private final CrowdSettingsGeneralService settingsService;
    private final DirectoriesService directoriesService;
    private final ApplicationsService applicationsService;

    public _AllServiceImpl(
            final CrowdSettingsGeneralService settingsService,
            final DirectoriesService directoriesService,
            final ApplicationsService applicationsService) {

        this.settingsService = settingsService;
        this.directoriesService = directoriesService;
        this.applicationsService = applicationsService;
    }

    @Override
    public _AllModel setAll(
            final _AllModel allModel) {

        final _AllModel result = new _AllModel();
        final Map<String, _AllModelStatus> statusMap = new HashMap<>();

        setEntity("settings", allModel.getSettings(), settingsService::setSettingsGeneral, result::setSettings, statusMap);

        setEntities("directories", allModel.getDirectories(), directoriesService::setDirectories, result::setDirectories, statusMap);

        setEntities("applications", allModel.getApplications(), applicationsService::setApplications, result::setApplications, statusMap);

        result.setStatus(statusMap);
        return result;
    }
}

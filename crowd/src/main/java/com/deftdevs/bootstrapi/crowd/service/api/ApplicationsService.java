package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;

import java.util.Map;

public interface ApplicationsService {

    Map<String, ApplicationModel> getApplications();

    ApplicationModel getApplication(
            long id);

    Map<String, ApplicationModel> setApplications(
            Map<String, ApplicationModel> applicationModels);

    ApplicationModel setApplication(
            long id,
            ApplicationModel applicationModel);

    ApplicationModel addApplication(
            ApplicationModel applicationModel);

    void deleteApplications(
            boolean force);

    void deleteApplication(
            long id);

}

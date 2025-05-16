package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;

import java.util.List;

public interface ApplicationsService {

    List<ApplicationModel> getApplications();

    ApplicationModel getApplication(
            long id);

    List<ApplicationModel> setApplications(
            List<ApplicationModel> applicationModels);

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

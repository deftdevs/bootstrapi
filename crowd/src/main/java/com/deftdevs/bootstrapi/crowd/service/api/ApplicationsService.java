package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;
import com.deftdevs.bootstrapi.crowd.model.ApplicationsBean;

public interface ApplicationsService {

    ApplicationsBean getApplications();

    ApplicationBean getApplication(
            long id);

    ApplicationsBean setApplications(
            ApplicationsBean applicationsBean);

    ApplicationBean setApplication(
            long id,
            ApplicationBean applicationBean);

    ApplicationBean addApplication(
            ApplicationBean applicationBean);

    void deleteApplications(
            boolean force);

    void deleteApplication(
            long id);

}

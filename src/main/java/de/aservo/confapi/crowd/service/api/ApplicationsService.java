package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.crowd.model.ApplicationBean;
import de.aservo.confapi.crowd.model.ApplicationsBean;

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

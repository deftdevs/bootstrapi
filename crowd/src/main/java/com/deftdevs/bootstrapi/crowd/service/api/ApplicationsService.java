package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;

import java.util.List;

public interface ApplicationsService {

    List<ApplicationBean> getApplications();

    ApplicationBean getApplication(
            long id);

    List<ApplicationBean> setApplications(
            List<ApplicationBean> applicationBeans);

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

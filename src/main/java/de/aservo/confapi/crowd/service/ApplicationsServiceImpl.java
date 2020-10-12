package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.ApplicationAlreadyExistsException;
import com.atlassian.crowd.exception.ApplicationNotFoundException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.manager.application.ApplicationManager;
import com.atlassian.crowd.manager.application.ApplicationManagerException;
import com.atlassian.crowd.model.application.Application;
import com.atlassian.crowd.model.application.ImmutableApplication;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.crowd.model.ApplicationBean;
import de.aservo.confapi.crowd.model.ApplicationsBean;
import de.aservo.confapi.crowd.model.util.ApplicationBeanUtil;
import de.aservo.confapi.crowd.service.api.ApplicationsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ExportAsService(ApplicationsService.class)
public class ApplicationsServiceImpl implements ApplicationsService {

    private final ApplicationManager applicationManager;

    @Inject
    public ApplicationsServiceImpl(
            @ComponentImport final ApplicationManager applicationManager) {

        this.applicationManager = applicationManager;
    }

    @Override
    public ApplicationsBean getApplications() {
        return new ApplicationsBean(applicationManager.findAll().stream()
                .map(ApplicationBeanUtil::toApplicationBean)
                .collect(Collectors.toList()));
    }

    @Override
    public ApplicationBean getApplication(
            final long id) {

        try {
            return ApplicationBeanUtil.toApplicationBean(applicationManager.findById(id));
        } catch (ApplicationNotFoundException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public ApplicationsBean setApplications(
            final ApplicationsBean applicationsBean) {

        final List<ApplicationBean> applicationBeans = new ArrayList<>();

        for (ApplicationBean applicationBean : applicationsBean.getApplications()) {
            try {
                final Application application = applicationManager.findByName(applicationBean.getName());
                applicationBeans.add(setApplication(application.getId(), applicationBean));
            } catch (ApplicationNotFoundException ignored) {
                applicationBeans.add(addApplication(applicationBean));
            }
        }

        return new ApplicationsBean(applicationBeans);
    }

    @Override
    public ApplicationBean setApplication(
            final long id,
            final ApplicationBean applicationBean) {

        try {
            final ImmutableApplication.Builder applicationBuilder = new ImmutableApplication.Builder(applicationManager.findById(id));

            if (applicationBean.getName() != null) {
                applicationBuilder.setName(applicationBean.getName());
            }
            if (applicationBean.getType() != null) {
                applicationBuilder.setType(ApplicationBeanUtil.toApplicationType(applicationBean.getType()));
            }
            if (applicationBean.getDescription() != null) {
                applicationBuilder.setDescription(applicationBean.getDescription());
            }
            if (applicationBean.getActive() != null) {
                applicationBuilder.setActive(applicationBean.getActive());
            }
            if (applicationBean.getPassword() != null) {
                applicationBuilder.setPasswordCredential(PasswordCredential.unencrypted(applicationBean.getPassword()));
            }

            return ApplicationBeanUtil.toApplicationBean(applicationManager.update(applicationBuilder.build()));
        } catch (ApplicationNotFoundException e) {
            throw new NotFoundException(e);
        } catch (ApplicationManagerException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public ApplicationBean addApplication(
            final ApplicationBean applicationBean) {

        try {
            final Application createdApplication = applicationManager.add(ApplicationBeanUtil.toApplication(applicationBean));
            return ApplicationBeanUtil.toApplicationBean(createdApplication);
        } catch (InvalidCredentialException | ApplicationAlreadyExistsException e) {
            throw new BadRequestException(e);
        }
    }

    @Override
    public void deleteApplications(
            final boolean force) {

        if (!force) {
            throw new BadRequestException("Set 'force' query param to true in order to delete all applications");
        }

        applicationManager.findAll().forEach(a -> deleteApplication(a.getId()));
    }

    @Override
    public void deleteApplication(
            final long id) {

        try {
            final Application application = applicationManager.findById(id);
            applicationManager.remove(application);
        } catch (ApplicationNotFoundException e) {
            throw new NotFoundException(e);
        } catch (ApplicationManagerException e) {
            throw new InternalServerErrorException(e);
        }
    }

}

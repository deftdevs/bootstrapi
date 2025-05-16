package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.impl.ImmutableDirectory;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdBean;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.jira.model.util.DirectoryBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class DirectoryServiceImpl implements DirectoriesService {

    private static final Logger log = LoggerFactory.getLogger(DirectoryServiceImpl.class);
    public static final int RETRY_AFTER_IN_SECONDS = 5;

    private final CrowdDirectoryService crowdDirectoryService;

    @Inject
    public DirectoryServiceImpl(@ComponentImport CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = crowdDirectoryService;
    }

    @Override
    public List<AbstractDirectoryBean> getDirectories() {
        List<AbstractDirectoryBean> beans = new ArrayList<>();
        for (Directory directory : crowdDirectoryService.findAllDirectories()) {
            AbstractDirectoryBean crowdBean;
            crowdBean = DirectoryBeanUtil.toDirectoryBean(directory);
            beans.add(crowdBean);
        }
        return beans;
    }

    @Override
    public AbstractDirectoryBean getDirectory(long id) {
        Directory directory = findDirectory(id);
        return DirectoryBeanUtil.toDirectoryBean(directory);
    }

    @Override
    public List<AbstractDirectoryBean> setDirectories(
            List<AbstractDirectoryBean> directoryBeans, boolean testConnection) {

        final Map<String, Directory> existingDirectoriesByName = crowdDirectoryService.findAllDirectories().stream()
                .collect(Collectors.toMap(Directory::getName, Function.identity()));

        for (AbstractDirectoryBean directoryRequestBean : directoryBeans) {
            if (directoryRequestBean instanceof DirectoryCrowdBean) {
                DirectoryCrowdBean crowdRequestBean = (DirectoryCrowdBean) directoryRequestBean;

                if (existingDirectoriesByName.containsKey(crowdRequestBean.getName())) {
                    setDirectory(existingDirectoriesByName.get(crowdRequestBean.getName()).getId(), crowdRequestBean, testConnection);
                } else {
                    addDirectory(crowdRequestBean, testConnection);
                }
            } else {
                throw new BadRequestException(format("Updating directory type '%s' is not supported (yet)", directoryRequestBean.getClass()));
            }
        };

        return getDirectories();
    }

    @Override
    public AbstractDirectoryBean setDirectory(long id, @NotNull AbstractDirectoryBean abstractDirectoryBean, boolean testConnection) {
        if (abstractDirectoryBean instanceof DirectoryCrowdBean) {
            return setDirectoryCrowd(id, (DirectoryCrowdBean) abstractDirectoryBean, testConnection);
        } else {
            throw new BadRequestException(format("Setting directory type '%s' is not supported (yet)", abstractDirectoryBean.getClass()));
        }
    }

    private AbstractDirectoryBean setDirectoryCrowd(long id, @NotNull DirectoryCrowdBean crowdBean, boolean testConnection) {
        Directory existingDirectory = findDirectory(id);
        Directory directory = validateAndCreateDirectoryConfig(crowdBean, testConnection);

        ImmutableDirectory.Builder directoryBuilder = ImmutableDirectory.newBuilder(existingDirectory);

        if (directory.getAttributes() != null) {
            directoryBuilder.setAttributes(directory.getAttributes());
        }

        if (directory.getDescription() != null) {
            directoryBuilder.setDescription(directory.getDescription());
        }

        if (directory.getName() != null) {
            directoryBuilder.setName(directory.getName());
        }

        directoryBuilder.setActive(directory.isActive());

        Directory updatedDirectory = directoryBuilder.toDirectory();

        Directory responseDirectory = crowdDirectoryService.updateDirectory(updatedDirectory);
        return DirectoryBeanUtil.toDirectoryBean(responseDirectory);
    }

    @Override
    public AbstractDirectoryBean addDirectory(AbstractDirectoryBean abstractDirectoryBean, boolean testConnection) {
        if (abstractDirectoryBean instanceof DirectoryCrowdBean) {
            DirectoryCrowdBean crowdBean = (DirectoryCrowdBean) abstractDirectoryBean;
            Directory directory = validateAndCreateDirectoryConfig(crowdBean, testConnection);
            Directory addedDirectory = crowdDirectoryService.addDirectory(directory);
            return DirectoryBeanUtil.toDirectoryBean(addedDirectory);
        } else {
            throw new BadRequestException(format("Adding directory type '%s' is not supported (yet)", abstractDirectoryBean.getClass()));
        }
    }

    @Override
    public void deleteDirectories(boolean force) {
        if (!force) {
            throw new BadRequestException("'force = true' must be supplied to delete all entries");
        } else {
            for (Directory directory : crowdDirectoryService.findAllDirectories()) {

                //do not remove the internal directory
                if (!DirectoryType.INTERNAL.equals(directory.getType())) {
                    deleteDirectory(directory.getId());
                }
            }
        }
    }

    @Override
    public void deleteDirectory(long id) {

        //ensure the directory exists
        findDirectory(id);

        //delete the directory
        try {
            crowdDirectoryService.removeDirectory(id);
        } catch (DirectoryCurrentlySynchronisingException e) {
            throw new ServiceUnavailableException(e, RETRY_AFTER_IN_SECONDS);
        }
    }

    private Directory findDirectory(long id) {
        Directory directory = crowdDirectoryService.findDirectoryById(id);
        if (directory == null) {
            throw new NotFoundException(String.format("directory with id '%s' was not found!", id));
        }
        return directory;
    }

    private Directory validateAndCreateDirectoryConfig(DirectoryCrowdBean crowdBean, boolean testConnection) {
        Directory directory = DirectoryBeanUtil.toDirectory(crowdBean);
        String directoryName = crowdBean.getName();
        if (testConnection) {
            log.debug("testing user directory connection for {}", directoryName);
            crowdDirectoryService.testConnection(directory);
        }
        return directory;
    }

}
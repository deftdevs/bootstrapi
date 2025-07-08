package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.impl.ImmutableDirectory;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import com.deftdevs.bootstrapi.commons.service.AbstractDirectoriesService;
import com.deftdevs.bootstrapi.jira.model.util.DirectoryModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

public class DirectoriesServiceImpl extends AbstractDirectoriesService {

    private static final Logger log = LoggerFactory.getLogger(DirectoriesServiceImpl.class);
    public static final int RETRY_AFTER_IN_SECONDS = 5;

    private final CrowdDirectoryService crowdDirectoryService;

    public DirectoriesServiceImpl(
            final CrowdDirectoryService crowdDirectoryService) {

        this.crowdDirectoryService = crowdDirectoryService;
    }

    @Override
    public List<AbstractDirectoryModel> getDirectories() {
        List<AbstractDirectoryModel> beans = new ArrayList<>();
        for (Directory directory : crowdDirectoryService.findAllDirectories()) {
            AbstractDirectoryModel crowdModel;
            crowdModel = DirectoryModelUtil.toDirectoryModel(directory);
            beans.add(crowdModel);
        }
        return beans;
    }

    @Override
    public AbstractDirectoryModel getDirectory(long id) {
        Directory directory = findDirectory(id);
        return DirectoryModelUtil.toDirectoryModel(directory);
    }

    public AbstractDirectoryModel setDirectory(long id, AbstractDirectoryModel directoryModel, boolean testConnection) {
        if (directoryModel instanceof DirectoryCrowdModel) {
            return setDirectoryCrowd(id, (DirectoryCrowdModel) directoryModel, testConnection);
        } else {
            throw new BadRequestException(format("Setting directory type '%s' is not supported (yet)", directoryModel.getClass()));
        }
    }

    @Override
    protected Set<Class<? extends AbstractDirectoryModel>> getSupportedClassesForUpdate() {
        return Set.of(DirectoryCrowdModel.class);
    }

    private AbstractDirectoryModel setDirectoryCrowd(long id, DirectoryCrowdModel crowdModel, boolean testConnection) {
        Directory existingDirectory = findDirectory(id);
        Directory directory = validateAndCreateDirectoryConfig(crowdModel, testConnection);

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
        return DirectoryModelUtil.toDirectoryModel(responseDirectory);
    }

    @Override
    public AbstractDirectoryModel addDirectory(AbstractDirectoryModel directoryModel, boolean testConnection) {
        if (directoryModel instanceof DirectoryCrowdModel) {
            DirectoryCrowdModel crowdModel = (DirectoryCrowdModel) directoryModel;
            Directory directory = validateAndCreateDirectoryConfig(crowdModel, testConnection);
            Directory addedDirectory = crowdDirectoryService.addDirectory(directory);
            return DirectoryModelUtil.toDirectoryModel(addedDirectory);
        } else {
            throw new BadRequestException(format("Adding directory type '%s' is not supported (yet)", directoryModel.getClass()));
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

    private Directory validateAndCreateDirectoryConfig(DirectoryCrowdModel crowdModel, boolean testConnection) {
        Directory directory = DirectoryModelUtil.toDirectory(crowdModel);
        String directoryName = crowdModel.getName();
        if (testConnection) {
            log.debug("testing user directory connection for {}", directoryName);
            crowdDirectoryService.testConnection(directory);
        }
        return directory;
    }

}
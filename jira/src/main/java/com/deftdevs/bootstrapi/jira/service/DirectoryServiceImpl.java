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
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.jira.model.util.DirectoryModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class DirectoryServiceImpl implements DirectoriesService {

    private static final Logger log = LoggerFactory.getLogger(DirectoryServiceImpl.class);
    public static final int RETRY_AFTER_IN_SECONDS = 5;

    private final CrowdDirectoryService crowdDirectoryService;

    public DirectoryServiceImpl(
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

    @Override
    public List<AbstractDirectoryModel> setDirectories(
            List<AbstractDirectoryModel> directoryModels, boolean testConnection) {

        final Map<String, Directory> existingDirectoriesByName = crowdDirectoryService.findAllDirectories().stream()
                .collect(Collectors.toMap(Directory::getName, Function.identity()));

        for (AbstractDirectoryModel directoryRequestModel : directoryModels) {
            if (directoryRequestModel instanceof DirectoryCrowdModel) {
                DirectoryCrowdModel crowdRequestModel = (DirectoryCrowdModel) directoryRequestModel;

                if (existingDirectoriesByName.containsKey(crowdRequestModel.getName())) {
                    setDirectory(existingDirectoriesByName.get(crowdRequestModel.getName()).getId(), crowdRequestModel, testConnection);
                } else {
                    addDirectory(crowdRequestModel, testConnection);
                }
            } else {
                throw new BadRequestException(format("Updating directory type '%s' is not supported (yet)", directoryRequestModel.getClass()));
            }
        };

        return getDirectories();
    }

    @Override
    public AbstractDirectoryModel setDirectory(long id, @NotNull AbstractDirectoryModel abstractDirectoryModel, boolean testConnection) {
        if (abstractDirectoryModel instanceof DirectoryCrowdModel) {
            return setDirectoryCrowd(id, (DirectoryCrowdModel) abstractDirectoryModel, testConnection);
        } else {
            throw new BadRequestException(format("Setting directory type '%s' is not supported (yet)", abstractDirectoryModel.getClass()));
        }
    }

    private AbstractDirectoryModel setDirectoryCrowd(long id, @NotNull DirectoryCrowdModel crowdModel, boolean testConnection) {
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
    public AbstractDirectoryModel addDirectory(AbstractDirectoryModel abstractDirectoryModel, boolean testConnection) {
        if (abstractDirectoryModel instanceof DirectoryCrowdModel) {
            DirectoryCrowdModel crowdModel = (DirectoryCrowdModel) abstractDirectoryModel;
            Directory directory = validateAndCreateDirectoryConfig(crowdModel, testConnection);
            Directory addedDirectory = crowdDirectoryService.addDirectory(directory);
            return DirectoryModelUtil.toDirectoryModel(addedDirectory);
        } else {
            throw new BadRequestException(format("Adding directory type '%s' is not supported (yet)", abstractDirectoryModel.getClass()));
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
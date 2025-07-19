package com.deftdevs.bootstrapi.confluence.service;

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
import com.deftdevs.bootstrapi.confluence.model.util.DirectoryModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Map<String, AbstractDirectoryModel> getDirectories() {
        return crowdDirectoryService.findAllDirectories().stream()
                .map(DirectoryModelUtil::toDirectoryModel)
                .collect(Collectors.toMap(AbstractDirectoryModel::getName, Function.identity()));
    }

    @Override
    public AbstractDirectoryModel getDirectory(long id) {
        Directory directory = findDirectory(id);
        return DirectoryModelUtil.toDirectoryModel(directory);
    }

    @Override
    public AbstractDirectoryModel setDirectory(
            final long id,
            final AbstractDirectoryModel directoryModel) {

        if (directoryModel instanceof DirectoryCrowdModel) {
            return setDirectoryCrowd(id, (DirectoryCrowdModel) directoryModel);
        } else {
            throw new BadRequestException(format("Setting directory type '%s' is not supported (yet)", directoryModel.getClass()));
        }
    }

    private AbstractDirectoryModel setDirectoryCrowd(
            long id,
            DirectoryCrowdModel crowdModel) {

        Directory existingDirectory = findDirectory(id);
        Directory directory = validateAndCreateDirectoryConfig(crowdModel);

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
    public AbstractDirectoryModel addDirectory(
            AbstractDirectoryModel directoryModel) {

        if (directoryModel instanceof DirectoryCrowdModel) {
            DirectoryCrowdModel crowdModel = (DirectoryCrowdModel) directoryModel;
            Directory directory = validateAndCreateDirectoryConfig(crowdModel);
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

    @Override
    protected Set<Class<? extends AbstractDirectoryModel>> getSupportedClassesForUpdate() {
        return Set.of(DirectoryCrowdModel.class);
    }

    private Directory findDirectory(long id) {
        Directory directory = crowdDirectoryService.findDirectoryById(id);
        if (directory == null) {
            throw new NotFoundException(String.format("directory with id '%s' was not found!", id));
        }
        return directory;
    }

    private Directory validateAndCreateDirectoryConfig(DirectoryCrowdModel crowdModel) {
        Directory directory = DirectoryModelUtil.toDirectory(crowdModel);
        String directoryName = crowdModel.getName();
        if (Boolean.TRUE.equals(crowdModel.getTestConnection())) {
            log.debug("testing user directory connection for {}", directoryName);
            crowdDirectoryService.testConnection(directory);
        }
        return directory;
    }
}

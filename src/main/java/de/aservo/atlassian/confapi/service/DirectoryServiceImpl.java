package de.aservo.atlassian.confapi.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.atlassian.confapi.model.DirectoryBean;
import de.aservo.atlassian.confapi.service.api.DirectoryService;
import de.aservo.atlassian.confapi.util.BeanValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The type User directory service.
 */
@Component
@ExportAsService({DirectoryService.class})
public class DirectoryServiceImpl implements DirectoryService {

    private static final Logger log = LoggerFactory.getLogger(DirectoryServiceImpl.class);

    private final CrowdDirectoryService crowdDirectoryService;

    /**
     * Instantiates a new User directory service.
     *
     * @param crowdDirectoryService the crowd directory service
     */
    @Inject
    public DirectoryServiceImpl(@ComponentImport CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = checkNotNull(crowdDirectoryService);
    }

    /**
     * Gets directories.
     *
     * @return the directories
     */
    public List<DirectoryBean> getDirectories() {
        return crowdDirectoryService.findAllDirectories().stream().map(DirectoryBean::from).collect(Collectors.toList());
    }

    /**
     * Adds a new directory configurations. Any existing configurations with the same 'name' property are removed before adding the new configuration.
     *
     * @param directoryBean      the directory
     * @param testConnection whether to test connection
     * @return the configuration added
     * @throws DirectoryCurrentlySynchronisingException the directory currently synchronising exception
     */
    public DirectoryBean addDirectory(DirectoryBean directoryBean, boolean testConnection) throws DirectoryCurrentlySynchronisingException {
        //preps and validation
        BeanValidationUtil.validate(directoryBean);
        Directory directory = directoryBean.toDirectory();
        String directoryName = directoryBean.getName();
        if (testConnection) {
            log.debug("testing user directory connection for {}", directoryName);
            crowdDirectoryService.testConnection(directory);
        }

        //check if directory exists already and if yes, remove it
        Optional<Directory> presentDirectory = crowdDirectoryService.findAllDirectories().stream().filter(dir -> dir.getName().equals(directory.getName())).findFirst();
        if (presentDirectory.isPresent()) {
            Directory presentDir = presentDirectory.get();
            log.info("removing existing user directory configuration '{}' before adding new configuration '{}'", presentDir.getName(), directory.getName());
            crowdDirectoryService.removeDirectory(presentDir.getId());
        }

        //add new directory
        return DirectoryBean.from(crowdDirectoryService.addDirectory(directory));
    }
}

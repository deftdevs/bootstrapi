package de.aservo.atlassian.confapi.service;

import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import de.aservo.atlassian.confapi.model.DirectoryBean;

import java.util.List;

/**
 * The User directory service interface.
 */
public interface DirectoryService {

    /**
     * Gets directories.
     *
     * @return the directories
     */
    List<DirectoryBean> getDirectories();

    /**
     * Adds a new directory configurations. Any existing configurations with the same 'name' property are removed before adding the new configuration.
     *
     * @param directory      the directory
     * @param testConnection whether to test connection
     * @return the configuration added
     * @throws DirectoryCurrentlySynchronisingException the directory currently synchronising exception
     */
    DirectoryBean addDirectory(DirectoryBean directory, boolean testConnection) throws DirectoryCurrentlySynchronisingException;
}

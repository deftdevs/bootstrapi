package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.DirectoryBean;

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
     * Adds a new directory configurations. Any existing configurations with the same 'name' property are removed before
     * adding the new configuration.
     *
     * @param directory      the directory
     * @param testConnection whether to test connection
     * @return the configuration added
     */
    DirectoryBean setDirectory(DirectoryBean directory, boolean testConnection);

}

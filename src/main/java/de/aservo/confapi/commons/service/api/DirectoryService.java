package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryBean;

/**
 * The User directory service interface.
 */
public interface DirectoryService {

    /**
     * Gets directories.
     *
     * @return the directories
     */
    DirectoriesBean getDirectories();

    /**
     * Adds new directory configurations. Any existing configurations with the same 'name' property are removed before
     * adding the new configurations.
     *
     * @param directories    the directories
     * @param testConnection whether to test connection
     * @return the directories
     */
    DirectoriesBean setDirectories(DirectoriesBean directories, boolean testConnection);

    /**
     * Adds a new directory configuration.
     *
     * @param directory      the directories
     * @param testConnection whether to test connection
     * @return the directories
     */
    DirectoriesBean addDirectory(DirectoryBean directory, boolean testConnection);
}

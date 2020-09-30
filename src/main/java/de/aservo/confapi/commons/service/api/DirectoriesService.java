package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoriesBean;

import javax.validation.constraints.NotNull;

/**
 * The User directory service interface.
 */
public interface DirectoriesService {

    /**
     * Gets directories.
     *
     * @return the directories
     */
    DirectoriesBean getDirectories();

    /**
     * Adds or Updates directory configurations. Any existing configurations with the same 'name' property is updated.
     *
     * @param directories    the directories
     * @param testConnection whether to test connection
     * @return the directories
     */
    DirectoriesBean setDirectories(
            @NotNull DirectoriesBean directories,
            boolean testConnection);

    /**
     * Updates a single directory configuration. Any existing configuration with the same 'name' property is updated.
     *
     * @param id             the directory id to update
     * @param directory      the directory
     * @param testConnection whether to test connection
     * @return the directories
     */
    AbstractDirectoryBean setDirectory(
            long id,
            @NotNull AbstractDirectoryBean directory,
            boolean testConnection);

    /**
     * Adds a new directory configuration.
     *
     * @param directory      the directories
     * @param testConnection whether to test connection
     * @return the added directory
     */
    AbstractDirectoryBean addDirectory(
            @NotNull AbstractDirectoryBean directory,
            boolean testConnection);
}

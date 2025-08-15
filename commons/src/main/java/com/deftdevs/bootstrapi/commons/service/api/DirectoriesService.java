package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;

import java.util.List;

/**
 * The User directory service interface.
 */
public interface DirectoriesService {

    /**
     * Gets all directories.
     *
     * @return the directories
     */
    List<AbstractDirectoryModel> getDirectories();

    /**
     * Gets a single directory.
     *
     * @param id the directory id to query
     * @return the directory
     */
    AbstractDirectoryModel getDirectory(
            final long id);

    /**
     * Adds or Updates directory configurations. Any existing configurations with the same 'name' property is updated.
     *
     * @param directories the directories
     * @return the directories
     */
    List<AbstractDirectoryModel> setDirectories(
            List<AbstractDirectoryModel> directories);

    /**
     * Updates a single directory configuration. Any existing configuration with the same 'name' property is updated.
     *
     * @param id             the directory id to update
     * @param directory      the directory
     * @param testConnection whether to test connection
     * @return the directories
     */
    AbstractDirectoryModel setDirectory(
            long id,
            AbstractDirectoryModel directory,
            boolean testConnection);

    /**
     * Adds a new directory configuration.
     *
     * @param directory      the directories
     * @param testConnection whether to test connection
     * @return the added directory
     */
    AbstractDirectoryModel addDirectory(
            AbstractDirectoryModel directory,
            boolean testConnection);

    /**
     * Deletes all directories
     *
     * @param force must be set to 'true' in order to delete all entries
     */
    void deleteDirectories(
            boolean force);

    /**
     * Deletes a single directory
     *
     * @param id the directory id to delete
     */
    void deleteDirectory(
            final long id);
}

package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;

import java.util.Map;

/**
 * The User directory service interface.
 */
public interface DirectoriesService {

    /**
     * Gets all directories.
     *
     * @return the directories
     */
    Map<String, ? extends AbstractDirectoryModel> getDirectories();

    /**
     * Gets a single directory.
     *
     * @param id the directory id to query
     * @return the directory
     */
    AbstractDirectoryModel getDirectory(
            final long id);

    /**
     * Adds or Updates directory configurations. Any existing configurations with the same key is updated.
     *
     * @param directoryModels the directories
     * @return the directories
     */
    Map<String, ? extends AbstractDirectoryModel> setDirectories(
            Map<String, ? extends AbstractDirectoryModel> directoryModels);

    /**
     * Updates a single directory configuration. Any existing configuration with the same 'name' property is updated.
     *
     * @param id             the directory id to update
     * @param directoryModel the directory
     * @return the directories
     */
    AbstractDirectoryModel setDirectory(
            long id,
            AbstractDirectoryModel directoryModel);

    /**
     * Adds a new directory configuration.
     *
     * @param directoryModel the directories
     * @return the added directory
     */
    AbstractDirectoryModel addDirectory(
            AbstractDirectoryModel directoryModel);

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

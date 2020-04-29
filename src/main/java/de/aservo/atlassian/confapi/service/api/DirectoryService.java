package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.exception.BadRequestException;
import de.aservo.atlassian.confapi.exception.InternalServerErrorException;
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
     * Adds a new directory configurations. Any existing configurations with the same 'name' property are removed before
     * adding the new configuration.
     *
     * @param directory      the directory
     * @param testConnection whether to test connection
     * @return the configuration added
     * @throws BadRequestException the bad request exception
     * @throws InternalServerErrorException the internal server error exception
     */
    DirectoryBean setDirectory(DirectoryBean directory, boolean testConnection)
            throws BadRequestException, InternalServerErrorException;

}

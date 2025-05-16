package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface ApplicationLinksService {

    /**
     * Gets all application links.
     *
     * @return the application links
     */
    List<ApplicationLinkModel> getApplicationLinks();

    /**
     * Gets a single application link.
     *
     * @param uuid the application link uuid query
     * @return the application link
     */
    ApplicationLinkModel getApplicationLink(
            final UUID uuid);

    /**
     * Sets or updates the given application links
     *
     * @param applicationLinkModels the application links to set / update
     * @param ignoreSetupErrors    whether or not to ignore authentication or other setup errors when setting up the link
     *                             which usually happens if the application to link has not setup the link counterpart yet
     * @return the updated application links
     */
    List<ApplicationLinkModel> setApplicationLinks(
            @NotNull final List<ApplicationLinkModel> applicationLinkModels,
            boolean ignoreSetupErrors);

    /**
     * Updates the given application link
     *
     * @param uuid                the application link uuid to update
     * @param applicationLinkModel the application link to set / update
     * @param ignoreSetupErrors   whether or not to ignore authentication or other setup errors when setting up the link
     *                            which usually happens if the application to link has not setup the link counterpart yet
     * @return the updated application link
     */
    ApplicationLinkModel setApplicationLink(
            @NotNull final UUID uuid,
            @NotNull final ApplicationLinkModel applicationLinkModel,
            boolean ignoreSetupErrors);

    /**
     * Adds a single application link
     *
     * @param applicationLinkModel the application link to set
     * @param ignoreSetupErrors   whether or not to ignore authentication or other setup errors when setting up the link
     *                            which usually happens if the application to link has not setup the link counterpart yet
     * @return the added application link
     */
    ApplicationLinkModel addApplicationLink(
            @NotNull final ApplicationLinkModel applicationLinkModel,
            boolean ignoreSetupErrors);

    /**
     * Deletes all application links
     *
     * @param force must be set to 'true' in order to delete all entries
     */
    void deleteApplicationLinks(
            boolean force);

    /**
     * Deletes a single application link
     *
     * @param uuid the application link uuid to delete
     */
    void deleteApplicationLink(
            @NotNull final UUID uuid);

}

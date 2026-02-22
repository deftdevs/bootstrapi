package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;

import java.util.Map;
import java.util.UUID;

public interface ApplicationLinksService {

    /**
     * Gets all application links.
     *
     * @return the application links
     */
    Map<String, ApplicationLinkModel> getApplicationLinks();

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
     * @return the updated application links
     */
    Map<String, ApplicationLinkModel> setApplicationLinks(
            final Map<String, ApplicationLinkModel> applicationLinkModels);

    /**
     * Updates the given application link
     *
     * @param uuid                 the application link uuid to update
     * @param applicationLinkModel the application link to set / update
     * @return the updated application link
     */
    ApplicationLinkModel setApplicationLink(
            final UUID uuid,
            final ApplicationLinkModel applicationLinkModel);

    /**
     * Adds a single application link
     *
     * @param applicationLinkModel the application link to set
     * @return the added application link
     */
    ApplicationLinkModel addApplicationLink(
            final ApplicationLinkModel applicationLinkModel);

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
            final UUID uuid);

}

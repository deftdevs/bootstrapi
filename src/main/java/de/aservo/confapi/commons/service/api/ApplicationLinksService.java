package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.ApplicationLinkBean;
import de.aservo.confapi.commons.model.ApplicationLinksBean;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ApplicationLinksService {

    /**
     * Gets the application links.
     *
     * @return the application links
     */
    public ApplicationLinksBean getApplicationLinks();


    /**
     * Sets or updates the given application links
     *
     * @param applicationLinksBean the application links to set / update
     * @param ignoreSetupErrors whether or not to ignore authentication or other setup errors when setting up the link
     *                          which usually happens if the application to link has not setup the link counterpart yet
     * @return the updated application links
     */
    public ApplicationLinksBean setApplicationLinks(
            @NotNull final ApplicationLinksBean applicationLinksBean,
            boolean ignoreSetupErrors);

    /**
     * Updates the given application link
     *
     * @param id the application link id to update
     * @param applicationLinkBean the application link to set / update
     * @param ignoreSetupErrors whether or not to ignore authentication or other setup errors when setting up the link
     *                          which usually happens if the application to link has not setup the link counterpart yet
     * @return the updated application link
     */
    public ApplicationLinkBean setApplicationLink(
            @NotNull final UUID id,
            @NotNull final ApplicationLinkBean applicationLinkBean,
            boolean ignoreSetupErrors);

    /**
     * Adds a single application link
     *
     * @param applicationLinkBean the application link to set
     * @param ignoreSetupErrors whether or not to ignore authentication or other setup errors when setting up the link
     *                          which usually happens if the application to link has not setup the link counterpart yet
     * @return the added application link
     */
    public ApplicationLinkBean addApplicationLink(
            @NotNull final ApplicationLinkBean applicationLinkBean,
            boolean ignoreSetupErrors);
    /**
     * Deletes all application links
     *
     * @param force must be set to 'true' in order to delete all entries
     */
    public void deleteApplicationLinks(
            boolean force);

    /**
     * Deletes a single application link
     *
     * @param id the application link id to delete
     */
    public void deleteApplicationLink(
            @NotNull final UUID id);
}

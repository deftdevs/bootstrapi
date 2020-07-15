package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.ApplicationLinkBean;
import de.aservo.confapi.commons.model.ApplicationLinksBean;

import javax.validation.constraints.NotNull;

public interface ApplicationLinksService {

    /**
     * Gets the application links.
     *
     * @return the application links
     */
    public ApplicationLinksBean getApplicationLinks();


    /**
     * Sets the application links
     *
     * @param applicationLinksBean the application links to set
     * @param ignoreSetupErrors whether or not to ignore authentication or other setup errors when setting up the link
     *                          which usually happens if the application to link has not setup the link counterpart yet
     * @return the application links
     */
    public ApplicationLinksBean setApplicationLinks(
            @NotNull final ApplicationLinksBean applicationLinksBean,
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
}

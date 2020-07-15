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
     * @return the application links
     */
    public ApplicationLinksBean setApplicationLinks(
            @NotNull final ApplicationLinksBean applicationLinksBean);

    /**
     * Adds a single application link
     *
     * @param applicationLinkBean the application link to set
     * @return the added application link
     */
    public ApplicationLinkBean addApplicationLink(
            @NotNull final ApplicationLinkBean applicationLinkBean);
}

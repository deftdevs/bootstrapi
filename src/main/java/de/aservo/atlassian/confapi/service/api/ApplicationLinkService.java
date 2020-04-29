package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.exception.BadRequestException;
import de.aservo.atlassian.confapi.exception.InternalServerErrorException;
import de.aservo.atlassian.confapi.model.ApplicationLinkBean;

import java.util.List;

/**
 * The Application link service interface.
 */
public interface ApplicationLinkService {

    /**
     * Gets application links.
     *
     * @return the application links
     */
    List<ApplicationLinkBean> getApplicationLinks();

    /**
     * Adds a new application link. NOTE: existing application links with the same type, e.g. "JIRA" will be
     * removed before adding the new configuration.
     *
     * @param linkBean the link bean
     * @return the added application link
     * @throws BadRequestException          the bad request exception
     * @throws InternalServerErrorException the internal server error exception
     */
    ApplicationLinkBean addApplicationLink(ApplicationLinkBean linkBean)
            throws BadRequestException, InternalServerErrorException;

}

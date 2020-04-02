package de.aservo.atlassian.confapi.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationException;
import com.atlassian.applinks.spi.manifest.ManifestNotFoundException;
import de.aservo.atlassian.confapi.model.ApplicationLinkBean;

import java.net.URISyntaxException;
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
     * @return the added application ,link
     * @throws URISyntaxException                   the uri syntax exception
     * @throws ManifestNotFoundException            the manifest not found exception
     * @throws AuthenticationConfigurationException the authentication configuration exception
     */
    ApplicationLink addApplicationLink(ApplicationLinkBean linkBean) throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException;
}

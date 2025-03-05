package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.internal.common.auth.oauth.ApplinksOAuth;
import com.atlassian.oauth.Consumer;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultServiceProviderStoreService {
    private final ServiceProviderConsumerStore serviceProviderConsumerStore;
    private final ServiceProviderTokenStore serviceProviderTokenStore;

    @Autowired
    public DefaultServiceProviderStoreService(ServiceProviderConsumerStore serviceProviderConsumerStore, ServiceProviderTokenStore serviceProviderTokenStore) {
        this.serviceProviderConsumerStore = serviceProviderConsumerStore;
        this.serviceProviderTokenStore = serviceProviderTokenStore;
    }

    public void addConsumer(Consumer consumer, ApplicationLink applicationLink) {
        this.serviceProviderConsumerStore.put(consumer);
        applicationLink.putProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY, consumer.getKey());
    }

    private String getConsumerKey(ApplicationLink applicationLink) {
        Object storedConsumerKey = applicationLink.getProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY);
        return storedConsumerKey != null ? storedConsumerKey.toString() : null;
    }

    public void removeConsumer(ApplicationLink applicationLink) {
        String consumerKey = this.getConsumerKey(applicationLink);
        if (consumerKey == null) {
            throw new IllegalStateException("No consumer configured for application link '" + applicationLink + "'.");
        } else {
            this.serviceProviderTokenStore.removeByConsumer(consumerKey);
            this.serviceProviderConsumerStore.remove(consumerKey);
            if (applicationLink.removeProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY) == null) {
                throw new IllegalStateException("Failed to remove consumer with key '" + consumerKey + "' from application link '" + applicationLink + "'");
            }
        }
    }

    public Consumer getConsumer(ApplicationLink applicationLink) {
        String consumerKey = this.getConsumerKey(applicationLink);
        return consumerKey != null ? this.serviceProviderConsumerStore.get(consumerKey) : null;
    }
}

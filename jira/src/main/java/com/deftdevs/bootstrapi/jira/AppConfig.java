package com.deftdevs.bootstrapi.jira;

import com.deftdevs.bootstrapi.jira.config.AtlassianConfig;
import com.deftdevs.bootstrapi.jira.config.HelperConfig;
import com.deftdevs.bootstrapi.jira.config.LifecycleConfig;
import com.deftdevs.bootstrapi.jira.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianConfig.class,
        HelperConfig.class,
        LifecycleConfig.class,
        ServiceConfig.class,
})
public class AppConfig {
}

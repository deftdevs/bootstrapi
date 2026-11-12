package com.deftdevs.bootstrapi.confluence;

import com.deftdevs.bootstrapi.commons.config.CloudConnectorConfig;
import com.deftdevs.bootstrapi.confluence.config.AtlassianConfig;
import com.deftdevs.bootstrapi.confluence.config.HelperConfig;
import com.deftdevs.bootstrapi.confluence.config.LifecycleConfig;
import com.deftdevs.bootstrapi.confluence.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianConfig.class,
        CloudConnectorConfig.class,
        HelperConfig.class,
        LifecycleConfig.class,
        ServiceConfig.class,
})
public class AppConfig {
}

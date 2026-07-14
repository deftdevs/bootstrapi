package com.deftdevs.bootstrapi.crowd;

import com.deftdevs.bootstrapi.crowd.config.AtlassianConfig;
import com.deftdevs.bootstrapi.crowd.config.HelperConfig;
import com.deftdevs.bootstrapi.crowd.config.LifecycleConfig;
import com.deftdevs.bootstrapi.crowd.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianConfig.class,
        HelperConfig.class,
        LifecycleConfig.class,
        ServiceConfig.class,
})
public class AppConfig {}

package com.deftdevs.bootstrapi.confluence;

import com.deftdevs.bootstrapi.confluence.config.AtlassianConfig;
import com.deftdevs.bootstrapi.confluence.config.HelperConfig;
import com.deftdevs.bootstrapi.confluence.config.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianConfig.class,
        HelperConfig.class,
        ServiceConfig.class,
})
public class AppConfig {
}

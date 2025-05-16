package com.deftdevs.bootstrapi.confluence;

import com.deftdevs.bootstrapi.confluence.beans.AtlassianBeans;
import com.deftdevs.bootstrapi.confluence.beans.HelperBeans;
import com.deftdevs.bootstrapi.confluence.beans.ServiceBeans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianBeans.class,
        HelperBeans.class,
        ServiceBeans.class,
})
public class AppBeans {
}

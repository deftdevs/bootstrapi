package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoriesBean;

import javax.annotation.Nullable;

public interface DirectoriesService {

    DirectoriesBean getDirectories();

    @Nullable
    AbstractDirectoryBean getDirectory(
            final long id);

}

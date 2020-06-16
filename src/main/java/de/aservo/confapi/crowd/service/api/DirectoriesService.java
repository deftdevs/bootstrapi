package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.crowd.model.DirectoryBean;

import javax.annotation.Nullable;

public interface DirectoriesService {

    @Nullable
    DirectoryBean getDirectory(
            final long id);

}

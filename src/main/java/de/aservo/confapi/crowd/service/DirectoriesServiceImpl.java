package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.crowd.model.DirectoryBean;
import de.aservo.confapi.crowd.model.util.DirectoryBeanUtil;
import de.aservo.confapi.crowd.service.api.DirectoriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(DirectoriesService.class)
public class DirectoriesServiceImpl implements DirectoriesService {

    private static final Logger log = LoggerFactory.getLogger(DirectoriesServiceImpl.class);

    @ComponentImport
    private final DirectoryManager directoryManager;

    @Inject
    public DirectoriesServiceImpl(
            final DirectoryManager directoryManager) {

        this.directoryManager = directoryManager;
    }

    @Override
    public DirectoryBean getDirectory(
            final long id) {

        try {
            final Directory directory = directoryManager.findDirectoryById(id);
            return DirectoryBeanUtil.toDirectoryBean(directory);
        } catch (DirectoryNotFoundException e) {
            log.info("Directory with id {} could not been found", id);
            return null;
        }
    }
}

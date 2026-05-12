package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractDirectoriesService implements DirectoriesService {

    @Override
    public Map<String, ? extends AbstractDirectoryModel> setDirectories(
            final Map<String, ? extends AbstractDirectoryModel> directoryModels) {

        final Map<String, AbstractDirectoryModel> existingDirectoriesByName = getDirectories().values().stream()
                .collect(Collectors.toMap(AbstractDirectoryModel::getName, Function.identity()));

        final Map<String, AbstractDirectoryModel> resultDirectories = new LinkedHashMap<>();

        for (Map.Entry<String, ? extends AbstractDirectoryModel> directoryModelEntry : directoryModels.entrySet()) {
            final String directoryName = directoryModelEntry.getKey();
            final AbstractDirectoryModel directoryModel = directoryModelEntry.getValue();
            final AbstractDirectoryModel existingDirectoryModel = existingDirectoriesByName.get(directoryName);

            if (directoryModel == null) {
                // declarative no-op: null model + existing entity → return as-is;
                // null model + missing entity → nothing to do
                if (existingDirectoryModel != null) {
                    resultDirectories.put(existingDirectoryModel.getName(), existingDirectoryModel);
                }
                continue;
            }

            if (directoryModel.getName() == null) {
                directoryModel.setName(directoryName);
            }

            // Check if directoryModel is not an instance of any supported class
            if (getSupportedClassesForUpdate().stream().noneMatch(clazz -> clazz.isInstance(directoryModel))) {
                throw new BadRequestException(String.format("Updating directory type '%s' is not supported (yet)", directoryModel.getClass()));
            }

            final AbstractDirectoryModel resultDirectoryModel;

            if (existingDirectoryModel != null) {
                resultDirectoryModel = setDirectory(existingDirectoryModel.getId(), directoryModel);
            } else {
                resultDirectoryModel = addDirectory(directoryModel);
            }

            resultDirectories.put(resultDirectoryModel.getName(), resultDirectoryModel);
        }

        return resultDirectories;
    }

    protected abstract Set<Class<? extends AbstractDirectoryModel>> getSupportedClassesForUpdate();

}

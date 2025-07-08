package com.deftdevs.bootstrapi.commons.service;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractDirectoriesService implements DirectoriesService {

    @Override
    public List<AbstractDirectoryModel> setDirectories(
            final List<AbstractDirectoryModel> directoryModels) {

        final Map<String, AbstractDirectoryModel> directoryModelMap = directoryModels.stream()
                .collect(Collectors.toMap(AbstractDirectoryModel::getName, Function.identity()));
        return new ArrayList<>(setDirectories(directoryModelMap).values());
    }

    @Override
    public Map<String, AbstractDirectoryModel> setDirectories(
            final Map<String, AbstractDirectoryModel> directoryModels) {

        final Map<String, AbstractDirectoryModel> existingDirectoriesByName = getDirectories().stream()
                .collect(Collectors.toMap(AbstractDirectoryModel::getName, Function.identity()));

        final Map<String, AbstractDirectoryModel> resultDirectories = new LinkedHashMap<>();

        for (Map.Entry<String, AbstractDirectoryModel> directoryModelEntry : directoryModels.entrySet()) {
            final AbstractDirectoryModel directoryModel = directoryModelEntry.getValue();

            // Check if directoryModel is not an instance of any supported class
            if (getSupportedClassesForUpdate().stream().noneMatch(clazz -> clazz.isInstance(directoryModel))) {
                throw new BadRequestException(String.format("Updating directory type '%s' is not supported (yet)", directoryModel.getClass()));
            }

            final AbstractDirectoryModel existingDirectoryModel = existingDirectoriesByName.get(directoryModelEntry.getKey());
            final AbstractDirectoryModel resultDirectoryModel;

            if (existingDirectoryModel != null) {
                resultDirectoryModel = setDirectory(existingDirectoryModel.getId(), directoryModelEntry.getValue(), false);
            } else {
                resultDirectoryModel = addDirectory(directoryModelEntry.getValue(), false);
            }

            resultDirectories.put(resultDirectoryModel.getName(), resultDirectoryModel);
        }

        return resultDirectories;
    }

    protected abstract Set<Class<? extends AbstractDirectoryModel>> getSupportedClassesForUpdate();

}

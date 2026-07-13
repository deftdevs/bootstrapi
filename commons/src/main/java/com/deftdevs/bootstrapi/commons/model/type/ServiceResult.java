package com.deftdevs.bootstrapi.commons.model.type;

import java.util.Map;

public class ServiceResult<T> {

    private final T model;
    private final Map<String, _AllModelStatus> status;

    public ServiceResult(
            final T model,
            final Map<String, _AllModelStatus> status) {

        this.model = model;
        this.status = status;
    }

    public T getModel() {
        return model;
    }

    public Map<String, _AllModelStatus> getStatus() {
        return status;
    }

}

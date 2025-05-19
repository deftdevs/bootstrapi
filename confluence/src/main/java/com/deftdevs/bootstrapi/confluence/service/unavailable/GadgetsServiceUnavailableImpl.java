package com.deftdevs.bootstrapi.confluence.service.unavailable;

import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import java.util.List;

public class GadgetsServiceUnavailableImpl implements GadgetsService {

    private static final String UNAVAILABLE_MESSAGE =
            "Gadgets functionality is not available. Please ensure the 'Gadgets for Confluence' plugin is installed and fully operational.";

    @Override
    public List<GadgetModel> getGadgets() {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }

    @Override
    public GadgetModel getGadget(long id) {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }

    @Override
    public List<GadgetModel> setGadgets(List<GadgetModel> gadgetModels) {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }

    @Override
    public GadgetModel setGadget(long id, GadgetModel gadgetModel) {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }

    @Override
    public GadgetModel addGadget(GadgetModel gadgetModel) {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }

    @Override
    public void deleteGadgets(boolean force) {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }

    @Override
    public void deleteGadget(long id) {
        throw new ServiceUnavailableException(UNAVAILABLE_MESSAGE);
    }
}

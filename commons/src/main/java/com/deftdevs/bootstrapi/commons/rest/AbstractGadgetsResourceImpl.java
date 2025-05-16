package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import com.deftdevs.bootstrapi.commons.rest.api.GadgetsResource;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractGadgetsResourceImpl implements GadgetsResource {

    private final GadgetsService gadgetsService;

    public AbstractGadgetsResourceImpl(final GadgetsService gadgetsService) {
        this.gadgetsService = gadgetsService;
    }

    @Override
    public Response getGadgets() {
        final List<GadgetModel> gadgetModels = gadgetsService.getGadgets();
        return Response.ok(gadgetModels).build();
    }

    @Override
    public Response setGadgets(
            final List<GadgetModel> gadgetModels) {
        List<GadgetModel> updatedGadgetModels = gadgetsService.setGadgets(gadgetModels);
        return Response.ok(updatedGadgetModels).build();
    }

    @Override
    public Response deleteGadgets(
            final boolean force) {
        gadgetsService.deleteGadgets(force);
        return Response.ok().build();
    }

}

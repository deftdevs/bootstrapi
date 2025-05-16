package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import com.deftdevs.bootstrapi.commons.rest.api.GadgetResource;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;

import javax.ws.rs.core.Response;

public abstract class AbstractGadgetResourceImpl implements GadgetResource {

    private final GadgetsService gadgetsService;

    public AbstractGadgetResourceImpl(final GadgetsService gadgetsService) {
        this.gadgetsService = gadgetsService;
    }

    @Override
    public Response getGadget(
            final long id) {
        final GadgetModel gadgetModel = gadgetsService.getGadget(id);
        return Response.ok(gadgetModel).build();
    }

    @Override
    public Response updateGadget(
            final long id,
            final GadgetModel gadgetModel) {
        GadgetModel updatedGadgetModel = gadgetsService.setGadget(
                id,
                gadgetModel);
        return Response.ok(updatedGadgetModel).build();
    }

    @Override
    public Response createGadget(
            final GadgetModel gadgetModel) {
        GadgetModel addedGadgetModel = gadgetsService.addGadget(gadgetModel);
        return Response.ok(addedGadgetModel).build();
    }

    @Override
    public Response deleteGadget(
            final long id) {
        gadgetsService.deleteGadget(id);
        return Response.ok().build();
    }
}

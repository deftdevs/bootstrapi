package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetBean;
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
        final GadgetBean gadgetBean = gadgetsService.getGadget(id);
        return Response.ok(gadgetBean).build();
    }

    @Override
    public Response updateGadget(
            final long id,
            final GadgetBean gadgetBean) {
        GadgetBean updatedGadgetBean = gadgetsService.setGadget(
                id,
                gadgetBean);
        return Response.ok(updatedGadgetBean).build();
    }

    @Override
    public Response createGadget(
            final GadgetBean gadgetBean) {
        GadgetBean addedGadgetBean = gadgetsService.addGadget(gadgetBean);
        return Response.ok(addedGadgetBean).build();
    }

    @Override
    public Response deleteGadget(
            final long id) {
        gadgetsService.deleteGadget(id);
        return Response.ok().build();
    }
}

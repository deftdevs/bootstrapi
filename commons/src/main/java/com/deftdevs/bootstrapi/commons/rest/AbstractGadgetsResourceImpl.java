package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetBean;
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
        final List<GadgetBean> gadgetBeans = gadgetsService.getGadgets();
        return Response.ok(gadgetBeans).build();
    }

    @Override
    public Response setGadgets(
            final List<GadgetBean> gadgetBeans) {
        List<GadgetBean> updatedGadgetBeans = gadgetsService.setGadgets(gadgetBeans);
        return Response.ok(updatedGadgetBeans).build();
    }

    @Override
    public Response deleteGadgets(
            final boolean force) {
        gadgetsService.deleteGadgets(force);
        return Response.ok().build();
    }

}

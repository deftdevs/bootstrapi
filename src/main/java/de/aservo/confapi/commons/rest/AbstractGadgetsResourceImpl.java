package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.GadgetBean;
import de.aservo.confapi.commons.model.GadgetsBean;
import de.aservo.confapi.commons.rest.api.GadgetsResource;
import de.aservo.confapi.commons.service.api.GadgetsService;

import javax.ws.rs.core.Response;

public abstract class AbstractGadgetsResourceImpl implements GadgetsResource {

    private final GadgetsService gadgetsService;

    public AbstractGadgetsResourceImpl(final GadgetsService gadgetsService) {
        this.gadgetsService = gadgetsService;
    }

    @Override
    public Response getGadgets() {
        final GadgetsBean gadgetsBean = gadgetsService.getGadgets();
        return Response.ok(gadgetsBean).build();
    }

    @Override
    public Response setGadgets(GadgetsBean gadgetsBean) {
        GadgetsBean updatedGadgetsBean = gadgetsService.setGadgets(gadgetsBean);
        return Response.ok(updatedGadgetsBean).build();
    }

    @Override
    public Response addGadget(GadgetBean gadgetBean) {
        GadgetBean addedGadgetBean = gadgetsService.addGadget(gadgetBean);
        return Response.ok(addedGadgetBean).build();
    }
}

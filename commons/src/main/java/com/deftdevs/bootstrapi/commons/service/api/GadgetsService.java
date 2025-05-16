package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.GadgetModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface GadgetsService {

    /**
     * Gets all gadgets.
     *
     * @return the gadgets
     */
    List<GadgetModel> getGadgets();

    /**
     * Gets a single gadget.
     *
     * @param id the gadget id to query
     * @return the gadget
     */
    GadgetModel getGadget(
            final long id);

    /**
     * Sets or updates the gadgets set.
     *
     * @param gadgetModels the gadget beans
     * @return the updated gadgets
     */
    List<GadgetModel> setGadgets(
            @NotNull final List<GadgetModel> gadgetModels);

    /**
     * Updates a single gadgets.
     *
     * @param id         the gadget id to update
     * @param gadgetModel the gadgets bean
     * @return the updated gadgets
     */
    GadgetModel setGadget(
            final long id,
            @NotNull final GadgetModel gadgetModel);

    /**
     * Add one single gadget.
     *
     * @param gadgetModel the gadget bean to add
     * @return the added gadget
     */
    GadgetModel addGadget(
            @NotNull final GadgetModel gadgetModel);

    /**
     * Deletes all gadgets
     *
     * @param force must be set to 'true' in order to delete all entries
     */
    void deleteGadgets(
            boolean force);

    /**
     * Deletes a single gadget
     *
     * @param id the gadget id to delete
     */
    void deleteGadget(
            final long id);

}

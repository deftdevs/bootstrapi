package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.GadgetBean;
import de.aservo.confapi.commons.model.GadgetsBean;

import javax.validation.constraints.NotNull;

public interface GadgetsService {

    /**
     * Gets all gadgets.
     *
     * @return the gadgets
     */
    public GadgetsBean getGadgets();

    /**
     * Gets a single gadget.
     *
     * @param id the gadget id to query
     *
     * @return the gadget
     */
    GadgetBean getGadget(
            final long id);

    /**
     * Sets or updates the gadgets set.
     *
     * @param gadgetsBean the gadgets bean
     * @return the updated gadgets
     */
    public GadgetsBean setGadgets(
            @NotNull final GadgetsBean gadgetsBean);

    /**
     * Updates a single gadgets.
     *
     * @param gadgetBean the gadgets bean
     * @return the updated gadgets
     */
    public GadgetBean setGadget(
            final long id,
            @NotNull final GadgetBean gadgetBean);

    /**
     * Add one single gadget.
     *
     * @param gadgetBean the gadget bean to add
     * @return the added gadget
     */
    public GadgetBean addGadget(
            @NotNull final GadgetBean gadgetBean);

    /**
     * Deletes all gadgets
     *
     * @param force must be set to 'true' in order to delete all entries
     */
    public void deleteGadgets(
            boolean force);

    /**
     * Deletes a single gadget
     *
     * @param id the gadget id to delete
     */
    public void deleteGadget(
            final long id);

}

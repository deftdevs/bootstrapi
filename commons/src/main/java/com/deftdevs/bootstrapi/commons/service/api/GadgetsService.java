package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.GadgetBean;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface GadgetsService {

    /**
     * Gets all gadgets.
     *
     * @return the gadgets
     */
    List<GadgetBean> getGadgets();

    /**
     * Gets a single gadget.
     *
     * @param id the gadget id to query
     * @return the gadget
     */
    GadgetBean getGadget(
            final long id);

    /**
     * Sets or updates the gadgets set.
     *
     * @param gadgetBeans the gadget beans
     * @return the updated gadgets
     */
    List<GadgetBean> setGadgets(
            @NotNull final List<GadgetBean> gadgetBeans);

    /**
     * Updates a single gadgets.
     *
     * @param id         the gadget id to update
     * @param gadgetBean the gadgets bean
     * @return the updated gadgets
     */
    GadgetBean setGadget(
            final long id,
            @NotNull final GadgetBean gadgetBean);

    /**
     * Add one single gadget.
     *
     * @param gadgetBean the gadget bean to add
     * @return the added gadget
     */
    GadgetBean addGadget(
            @NotNull final GadgetBean gadgetBean);

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

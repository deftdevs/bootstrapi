package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.model.GadgetBean;
import de.aservo.atlassian.confapi.model.GadgetsBean;

import javax.validation.constraints.NotNull;

public interface GadgetsService {

    /**
     * Get the gadgets.
     *
     * @return the gadgets
     */
    public GadgetsBean getGadgets();

    /**
     * Update the gadgets set.
     *
     * @param gadgetsBean the gadgets bean
     * @return the updated gadgets
     */
    public GadgetsBean setGadgets(
            @NotNull final GadgetsBean gadgetsBean);

    /**
     * Add one single gadget.
     *
     * @param gadgetBean the gadget bean to add
     * @return the updated gadgets
     */
    public GadgetsBean addGadget(
            @NotNull final GadgetBean gadgetBean);

}

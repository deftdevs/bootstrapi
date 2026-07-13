package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractSettingsModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;

/**
 * Composite settings access: the whole settings group of a product in one
 * call. The product settings service interfaces implement both methods with
 * default methods that dispatch to the individual settings services.
 *
 * @param <S> the product's settings model type
 */
public interface SettingsService<S extends AbstractSettingsModel> {

    S getSettings();

    ServiceResult<S> setSettings(S settingsModel);

}

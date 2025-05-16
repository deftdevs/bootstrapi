package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.gadgets.GadgetParsingException;
import com.atlassian.gadgets.GadgetRequestContext;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpec;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecId;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GadgetsServiceImpl implements GadgetsService {

    private static final Logger log = LoggerFactory.getLogger(GadgetsServiceImpl.class);
    
    private final ExternalGadgetSpecStore externalGadgetSpecStore;
    private final GadgetSpecFactory gadgetSpecFactory;
    private final LocaleManager localeManager;

    public GadgetsServiceImpl(
            final ExternalGadgetSpecStore externalGadgetSpecStore,
            final GadgetSpecFactory gadgetSpecFactory,
            final LocaleManager localeManager) {

        this.externalGadgetSpecStore = externalGadgetSpecStore;
        this.gadgetSpecFactory = gadgetSpecFactory;
        this.localeManager = localeManager;
    }

    @Override
    public List<GadgetModel> getGadgets() {
        Iterable<ExternalGadgetSpec> specIterable = externalGadgetSpecStore.entries();
        return StreamSupport.stream(specIterable.spliterator(), false)
                .map(spec -> {
                    GadgetModel gadgetModel = new GadgetModel();
                    gadgetModel.setId(Long.valueOf(spec.getId().value()));
                    gadgetModel.setUrl(spec.getSpecUri());
                    return gadgetModel;
                }).collect(Collectors.toList());
    }

    @Override
    public GadgetModel getGadget(long id) {
        return findGadget(id);
    }

    @Override
    public List<GadgetModel> setGadgets(List<GadgetModel> gadgetModels) {
        //as the gadget only consists of an url, only new gadgets need to be added, existing gadget urls remain
        List<GadgetModel> existingGadgets = getGadgets();
        gadgetModels.forEach(gadgetModel -> {
            Optional<GadgetModel> gadget = existingGadgets.stream()
                    .filter(bean -> bean.getUrl().toString().equals(gadgetModel.getUrl().toString())).findFirst();
            if (!gadget.isPresent()) {
                addGadget(gadgetModel);
            }
        });
        return getGadgets();
    }

    @Override
    public GadgetModel setGadget(long id, @NotNull GadgetModel gadgetModel) {
        deleteGadget(id);
        return addGadget(gadgetModel);
    }

    @Override
    public GadgetModel addGadget(GadgetModel gadgetModel) {
        URI url = gadgetModel.getUrl();
        GadgetModel addedGadgetModel = new GadgetModel();
        ExternalGadgetSpec addedGadget = externalGadgetSpecStore.add(url);
        try{
            //validate gadget url
            log.debug("testing external gadget link url for validity: {}", url);

            ConfluenceUser user = AuthenticatedUserThreadLocal.get();
            Locale locale = localeManager.getLocale(user);
            GadgetRequestContext requestContext = GadgetRequestContext.Builder.gadgetRequestContext()
                    .locale(locale)
                    .ignoreCache(true)
                    .user(new GadgetRequestContext.User(user.getKey().getStringValue(), user.getName()))
                    .build();
            gadgetSpecFactory.getGadgetSpec(url, requestContext);

            addedGadgetModel.setUrl(addedGadget.getSpecUri());
        } catch (GadgetParsingException e) {
            externalGadgetSpecStore.remove(addedGadget.getId());
            throw new BadRequestException("Invalid Gadget URL");
        }
        return addedGadgetModel;
    }

    @Override
    public void deleteGadgets(boolean force) {
        if (!force) {
            throw new BadRequestException("'force = true' must be supplied to delete all entries");
        } else {
            externalGadgetSpecStore.entries().forEach(gadget -> externalGadgetSpecStore.remove(gadget.getId()));
        }
    }

    @Override
    public void deleteGadget(long id) {

        //ensure gadget exists
        findGadget(id);

        //remove gadget
        externalGadgetSpecStore.remove(ExternalGadgetSpecId.valueOf(String.valueOf(id)));
    }

    private GadgetModel findGadget(long id) {
        Optional<GadgetModel> result = getGadgets().stream().filter(gadget -> gadget.getId().equals(id)).findFirst();
        if (!result.isPresent()) {
            throw new NotFoundException(String.format("gadget with id '%s' could not be found", id));
        } else {
            return result.get();
        }
    }
}

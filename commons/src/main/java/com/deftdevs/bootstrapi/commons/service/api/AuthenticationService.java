package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public interface AuthenticationService {

    default AuthenticationModel getAuthentication() {
        return new AuthenticationModel(
                new LinkedHashMap<>(getAuthenticationIdps()),
                getAuthenticationSso());
    }

    default ServiceResult<AuthenticationModel> setAuthentication(final AuthenticationModel authenticationModel) {
        final AuthenticationModel result = new AuthenticationModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, AbstractAuthenticationIdpModel.class, authenticationModel.getIdps(),
                idps -> new LinkedHashMap<>(setAuthenticationIdps(idps)), result::setIdps);
        ServiceResultUtil.setSubEntity(status, AuthenticationSsoModel.class, authenticationModel.getSso(),
                this::setAuthenticationSso, result::setSso);

        return new ServiceResult<>(result, status);
    }

    Map<String, ? extends AbstractAuthenticationIdpModel> getAuthenticationIdps();

    Map<String, ? extends AbstractAuthenticationIdpModel> setAuthenticationIdps(
            Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels);

    AbstractAuthenticationIdpModel setAuthenticationIdp(
            AbstractAuthenticationIdpModel authenticationIdpModel);

    AuthenticationSsoModel getAuthenticationSso();

    AuthenticationSsoModel setAuthenticationSso(
            AuthenticationSsoModel authenticationSsoModel);

}

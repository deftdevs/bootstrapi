package com.atlassian.confluence.settings.setup;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class DefaultSingleProductLicenseDetailsView implements SingleProductLicenseDetailsView {

    private final LicenseModel licenseModel;

    public DefaultSingleProductLicenseDetailsView(LicenseModel licenseModel) {
        this.licenseModel = licenseModel;
    }

    @Override
    public boolean isEvaluationLicense() {
        return false;
    }

    @Nonnull
    @Override
    public String getLicenseTypeName() {
        return licenseModel.getType();
    }

    @Override
    public String getOrganisationName() {
        return licenseModel.getOrganization();
    }

    @Nullable
    @Override
    public String getSupportEntitlementNumber() {
        return null;
    }

    @Override
    public String getDescription() {
        return licenseModel.getDescription();
    }

    @Override
    public String getServerId() {
        return null;
    }

    @Override
    public boolean isPerpetualLicense() {
        return false;
    }

    @Nullable
    @Override
    public Date getLicenseExpiryDate() {
        return licenseModel.getExpiryDate();
    }

    @Nullable
    @Override
    public Date getMaintenanceExpiryDate() {
        return null;
    }

    @Override
    public boolean isDataCenter() {
        return false;
    }

    @Override
    public boolean isEnterpriseLicensingAgreement() {
        return false;
    }

    @Nonnull
    @Override
    public String getProductKey() {
        return null;
    }

    @Override
    public boolean isUnlimitedNumberOfUsers() {
        return false;
    }

    @Override
    public int getNumberOfUsers() {
        return licenseModel.getMaxUsers();
    }

    @Nonnull
    @Override
    public String getProductDisplayName() {
        return licenseModel.getProducts().iterator().next();
    }

    @Nullable
    @Override
    public String getProperty(@Nonnull String property) {
        return null;
    }
}

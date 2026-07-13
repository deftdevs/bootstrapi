package com.deftdevs.bootstrapi.commons.constants;

public class BootstrAPI {

    public static final String _ALL                         = "_all";
    public static final String _ROOT                        = "/";

    public static final String APPLICATION                  = "application";
    public static final String APPLICATIONS                 = "applications";
    public static final String APPLICATION_LINK             = "application-link";
    public static final String APPLICATION_LINKS            = "application-links";
    public static final String AUTHENTICATION               = "authentication";
    public static final String AUTHENTICATION_IDP           = "idp";
    public static final String AUTHENTICATION_IDPS          = "idps";
    public static final String AUTHENTICATION_IDP_OIDC      = "oidc";
    public static final String AUTHENTICATION_IDP_SAML      = "saml";
    public static final String AUTHENTICATION_SSO           = "sso";
    public static final String BACKUP                       = "backup";
    public static final String CACHE                        = "cache";
    public static final String CACHES                       = "caches";
    public static final String CACHE_FLUSH                  = "flush";
    public static final String COLOR_SCHEME                 = "color-scheme";
    public static final String DIRECTORIES                  = "directories";
    public static final String DIRECTORY                    = "directory";
    public static final String DIRECTORY_CROWD              = "crowd";
    public static final String DIRECTORY_DELEGATING         = "delegating";
    public static final String DIRECTORY_GENERIC            = "generic";
    public static final String DIRECTORY_INTERNAL           = "internal";
    public static final String DIRECTORY_LDAP               = "ldap";
    public static final String ERROR                        = "error";
    public static final String ERRORS                       = "errors";
    public static final String FAVICON                      = "favicon";
    public static final String GROUP                        = "group";
    public static final String GROUPS                       = "groups";
    public static final String HTTP                         = "http";
    public static final String LICENSE                      = "license";
    public static final String LICENSES                     = "licenses";
    public static final String LOGO                         = "logo";
    public static final String MAIL_SERVER                  = "mail-server";
    public static final String MAIL_SERVERS                 = "mail-servers";
    public static final String MAIL_SERVER_POP              = "pop";
    public static final String MAIL_SERVER_SMTP             = "smtp";
    public static final String MAIL_TEMPLATES               = "mail-templates";
    public static final String PERMISSION                   = "permission";
    public static final String PERMISSIONS                  = "permissions";
    public static final String PERMISSION_ANONYMOUS_ACCESS  = "anonymous-access";
    public static final String PERMISSIONS_GLOBAL           = "global";
    public static final String PING                         = "ping";
    public static final String SESSION_CONFIG               = "session-config";
    public static final String SETTINGS                     = "settings";
    public static final String SETTINGS_BRANDING            = "branding";
    public static final String SETTINGS_BRANDING_BANNER     = "banner";
    public static final String SETTINGS_BRANDING_CUSTOM_HTML= "custom-html";
    public static final String SETTINGS_BRANDING_LOGIN_PAGE = "login-page";
    public static final String SETTINGS_BRANDING_LOGO       = "logo";
    public static final String SETTINGS_GENERAL             = "general";
    public static final String SETTINGS_SECURITY            = "security";
    public static final String TRUSTED_PROXIES              = "trusted-proxies";
    public static final String USER                         = "user";
    public static final String USERS                        = "users";
    public static final String USER_PASSWORD                = "password";

    public static final String MEDIA_TYPE_IMAGE             = "image/*";
    public static final String MEDIA_TYPE_YAML              = "application/yaml";
    public static final String MEDIA_TYPE_YAML_LEGACY       = "application/x-yaml";
    public static final String MEDIA_TYPE_YAML_TEXT         = "text/yaml";

    public static final String ERROR_COLLECTION_RESPONSE_DESCRIPTION      = "Returns a list of error messages.";
    public static final String _ALL_PUT_SUMMARY                           = "Apply a complete configuration";
    public static final String _ALL_PUT_RESPONSE_DESCRIPTION              = "Returns the updated configuration. The per-sub-field outcome is reported in the"
            + " 'status' map, keyed by the request's field paths (e.g. 'settings/general', 'mailServer/smtp';"
            + " 2xx for success, 4xx/5xx for failure with a human-readable 'message' and optional 'details')."
            + " License keys in the response are redacted (e.g. 'AAAB...wxyz#a1b2')."
            + " If any sub-field fails, the highest sub-field status code is returned with the same response body.";
    public static final String _ALL_PUT_FAILURE_RESPONSE_DESCRIPTION      = "One or more sub-fields failed to apply. The response code is the highest"
            + " sub-field status code; inspect the per-sub-field 'status' map in the response body to see which"
            + " fields succeeded and which failed. Request-level errors (e.g. a missing request body) return an"
            + " error message list instead.";
    public static final String SETTINGS_GET_SUMMARY                       = "Get all settings";
    public static final String SETTINGS_GET_RESPONSE_DESCRIPTION          = "Returns all settings groups supported by this product.";
    public static final String SETTINGS_PUT_SUMMARY                       = "Apply a settings configuration";
    public static final String SETTINGS_PUT_RESPONSE_DESCRIPTION          = "Returns the updated settings. The per-sub-field outcome is reported in the"
            + " 'status' map, keyed by the request's field paths (e.g. 'general', 'branding/colorScheme')."
            + " If any sub-field fails, the highest sub-field status code is returned with the same response body.";
    public static final String SETTINGS_GENERAL_GET_SUMMARY               = "Get the general settings";
    public static final String SETTINGS_GENERAL_GET_RESPONSE_DESCRIPTION  = "Returns the general settings";
    public static final String SETTINGS_GENERAL_PUT_SUMMARY               = "Set the general settings";
    public static final String SETTINGS_GENERAL_PUT_RESPONSE_DESCRIPTION  = "Returns the updated general settings";
    public static final String SETTINGS_SECURITY_GET_SUMMARY              = "Get the security settings";
    public static final String SETTINGS_SECURITY_GET_RESPONSE_DESCRIPTION = "Returns the security settings";
    public static final String SETTINGS_SECURITY_PUT_SUMMARY              = "Set the security settings";
    public static final String SETTINGS_SECURITY_PUT_RESPONSE_DESCRIPTION = "Returns the updated security settings";

    private BootstrAPI() {
    }

}

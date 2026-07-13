BootstrAPI
==========

[![Build Status](https://github.com/deftdevs/bootstrapi/actions/workflows/ci.yaml/badge.svg)](https://github.com/deftdevs/bootstrapi/actions/workflows/ci.yaml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=deftdevs_bootstrapi&metric=coverage)](https://sonarcloud.io/dashboard?id=deftdevs_bootstrapi)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=deftdevs_bootstrapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=deftdevs_bootstrapi)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

BootstrAPI is a series of plugins for providing additional REST API's for Atlassian products with focus on initial and declarative instance configuration (also referred to as "bootstrapping", hence the name).

## Example

The same configuration models work across all products. A minimal configuration applying the general settings and an SMTP mail server:

```yaml
# config.yaml
settings:
  general:
    title: Example
    baseUrl: https://confluence.example.com
mailServer:
  smtp:
    from: mail@example.com
    host: mail.example.com
    port: 25
```

Apply it in a single request via the `_all` endpoint (the REST root), e.g. with [yq](https://github.com/mikefarah/yq) and curl:

```sh
yq -o=json config.yaml | curl -u "$ADMIN_USERNAME:$ADMIN_PASSWORD" -X PUT -H "Content-Type: application/json" -d @- \
    https://confluence.example.com/rest/bootstrapi/1/
```

The very same document — with the product's own base URL — configures Jira via `https://jira.example.com/rest/bootstrapi/1/` and Crowd via `https://crowd.example.com/crowd/rest/bootstrapi/1/`. Product-specific sub-fields (e.g. Jira's `settings.branding.banner` or Crowd's `trustedProxies`) can simply be added to the same document.

Every sub-field present in the request is applied independently: the response echoes the resulting configuration and reports each sub-field's outcome in its `status` map, keyed by the request's field paths (e.g. `settings/general`, `mailServer/smtp`), answering with the highest sub-field status code.

## Plugins

- [Confluence](confluence) ([documentation](confluence/README.md))
- [Crowd](crowd) ([documentation](crowd/README.md))
- [Jira](jira) ([documentation](jira/README.md))

## About

BootstrAPI is an open source project of Deft Devs LLC.
The project originated from the [ConfAPI](https://github.com/aservo/confapi-commons/) project.
This fork has been created by ConfAPI's [original maintainer](https://github.com/pathob).

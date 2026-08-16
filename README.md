BootstrAPI
==========

[![Build Status](https://github.com/deftdevs/bootstrapi/actions/workflows/ci.yaml/badge.svg)](https://github.com/deftdevs/bootstrapi/actions/workflows/ci.yaml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=deftdevs_bootstrapi&metric=coverage)](https://sonarcloud.io/dashboard?id=deftdevs_bootstrapi)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=deftdevs_bootstrapi&metric=alert_status)](https://sonarcloud.io/dashboard?id=deftdevs_bootstrapi)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](CONTRIBUTING.md)

BootstrAPI is a series of plugins for providing additional REST APIs for Atlassian products with focus on initial and declarative instance configuration (also referred to as "bootstrapping", hence the name). Configuration documents are declarative and idempotent - applying the same document again is safe - which makes the API a natural fit for automated provisioning.

## Plugins

- [Confluence](confluence) ([documentation](confluence/README.md))
- [Crowd](crowd) ([documentation](crowd/README.md))
- [Jira](jira) ([documentation](jira/README.md))

## Example

The same configuration models work across all products. A minimal configuration applying the general settings and an SMTP mail server, in a single request via the `_all` endpoint (the REST root) - YAML is accepted directly, JSON works just as well:

```sh
curl -u "$ADMIN_USERNAME:$ADMIN_PASSWORD" -X PUT -H "Content-Type: application/yaml" \
    --data-binary @- https://confluence.example.com/rest/bootstrapi/1/ <<EOF
settings:
  general:
    title: Example
    baseUrl: https://confluence.example.com
mailServer:
  smtp:
    from: mail@example.com
    host: mail.example.com
    port: 25
EOF
```

The very same document - with the product's own base URL - configures Jira via `https://jira.example.com/rest/bootstrapi/1/` and Crowd via `https://crowd.example.com/crowd/rest/bootstrapi/1/`. Product-specific sub-fields (e.g. Jira's `settings.branding.banner` or Crowd's `trustedProxies`) can simply be added to the same document.

Every sub-field present in the request is applied independently: the response echoes the resulting configuration and reports each sub-field's outcome in its `status` map, keyed by the request's field paths (e.g. `settings/general`, `mailServer/smtp`), answering with the highest sub-field status code.

## Startup configuration

Instead of (or in addition to) calling the REST API, the plugins can apply a configuration automatically during application startup. Place a `bootstrapi.yaml` file with the same structure as the `_all` request body into the application's shared home directory (or local home directory) and it is applied as soon as the application has started.

The startup configuration is cluster-safe: only one node applies the file at a time, and the hash of the last successfully applied document is recorded in the database, so restarts and additional replicas skip an unchanged file.

A configuration that cannot be read, parsed or fully applied stops the application, so an instance never comes up with a configuration it could not reach - in a rolling deployment this blocks the rollout instead of hiding the failure. Since only successful applies are recorded, the configuration is retried when the instance is started again.

## Instance setup

The plugin JARs double as setup tools: running `java -jar bootstrapi-<product>-plugin.jar` drives the product setup wizard of a freshly installed instance over HTTP, configured through `BOOTSTRAPI_SETUP_*` environment variables (base URL, license, database connection, administrator account). Together with the startup configuration this makes a complete instance bootstrap declarative: a deployment hook runs the setup from the same artifact that is installed as the plugin, and the `bootstrapi.yaml` applies everything else the moment the setup completes.

The Crowd and Jira wizards are driven completely, including the database step. For Confluence the database connection and license must already be configured, e.g. through the `ATL_*` environment variables of the official container images.

## Installation

Download the plugin for your product from the [releases](https://github.com/deftdevs/bootstrapi/releases) and upload it in the product's administration under *Manage apps* → *Upload app*. The endpoints require a user with system administrator permissions.

## Compatibility

The plugins are built and tested against Confluence 9.x, Jira 10.x and Crowd 6.x (Data Center).

## About

BootstrAPI is an open source project of Deft Devs LLC.
The project originated from the [ConfAPI](https://github.com/aservo/confapi-commons/) project.
This fork has been created by ConfAPI's [original maintainer](https://github.com/pathob).

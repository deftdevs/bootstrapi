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

## Plugin installation

The `upm` section installs other plugins declaratively, through the plugin framework itself - no UPM tokens or admin credentials involved. Resolvers and plugins are both keyed maps, so merged YAML documents can override single values. Every plugin references one of the named resolvers explicitly: `marketplace` type resolvers look the artifact up through the Atlassian Marketplace REST API from the plugin key and version alone, `maven` type resolvers derive it from the plugin's Maven coordinates and the standard repository layout.

```yaml
upm:
  resolvers:
    marketplace:
      type: marketplace
      baseUrl: https://marketplace.atlassian.com
    corp:
      type: maven
      baseUrl: https://repository.example.com/maven
      username: reader
      password: secret
  plugins:
    de.griffel.confluence.plugins.plant-uml:
      version: "2026.103"
      resolver: marketplace
    com.example.internal-plugin:
      version: 1.0.0
      resolver: corp
      groupId: com.example
      artifactId: internal-plugin
```

A resolver's base URL may point to a proxying repository such as an Artifactory generic remote: all links returned by the Marketplace API are re-resolved against the resolver's base URL, so the API lookup and the binary download flow through the same repository (resolver credentials work for either type). For endpoints only reachable through a forward proxy, each resolver takes an optional `proxy` (`host`, `port` and optional credentials), so an internal repository and a proxied external one can coexist in the same document.

Plugins already installed in the requested version are skipped, `enabled: false` disables a plugin, and responses echo only the plugins map - never the resolver credentials. Like every other section, `upm` works via the REST API (`PUT /upm` or as part of `_all`) and the startup configuration alike.

## Installation

Download the plugin for your product from the [releases](https://github.com/deftdevs/bootstrapi/releases) and upload it in the product's administration under *Manage apps* → *Upload app*. The endpoints require a user with system administrator permissions.

## Compatibility

The plugins are built and tested against Confluence 9.x, Jira 10.x and Crowd 6.x (Data Center).

## About

BootstrAPI is an open source project of Deft Devs LLC.
The project originated from the [ConfAPI](https://github.com/aservo/confapi-commons/) project.
This fork has been created by ConfAPI's [original maintainer](https://github.com/pathob).

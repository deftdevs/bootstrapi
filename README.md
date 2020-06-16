[![ASERVO Software GmbH](https://aservo.github.io/img/aservo_atlassian_banner.png)](https://www.aservo.com/en/atlassian)

ConfAPI for Crowd
=================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.aservo/confapi-crowd-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.aservo/confapi-crowd-plugin)
[![Build Status](https://circleci.com/gh/aservo/confapi-crowd-plugin.svg?style=shield)](https://circleci.com/gh/aservo/confapi-crowd-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aservo_confapi-crowd-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=aservo_confapi-crowd-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aservo_confapi-crowd-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=aservo_confapi-crowd-plugin)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

REST API for automated Crowd configuration.

Related Documentation
---------------------

* [Atlassian REST API design guidelines version 1](https://developer.atlassian.com/server/framework/atlassian-sdk/atlassian-rest-api-design-guidelines-version-1/)

Resources
---------

All resources produce JSON (media type:  `application/json`) results.

### Directory

Access Crowd directories and their attributes

* #### `GET /rest/confapi/1/directory/{id}`

  Get Crowd directory details and attributes for given directory ID.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "id": 12345,
    "name": "directory",
    "attributes": {
      "passwordRegex":"",
      "passwordComplexityMessage":"",
      "passwordMaxAttempts":10,
      "passwordHistoryCount":3,
      "passwordMaxChangeTime":60
    }
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

  ![Status 404][status-404]

  Returned if the directory ID could not be found.

* #### `GET /rest/confapi/1/directory/{id}/attributes`

  Get Crowd directory attributes for given directory ID.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "passwordRegex":"",
    "passwordComplexityMessage":"",
    "passwordMaxAttempts":10,
    "passwordHistoryCount":3,
    "passwordMaxChangeTime":60
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

  ![Status 404][status-404]

  Returned if the directory ID could not be found.


[status-200]: https://img.shields.io/badge/status-200-brightgreen.svg
[status-400]: https://img.shields.io/badge/status-400-red.svg
[status-401]: https://img.shields.io/badge/status-401-red.svg
[status-403]: https://img.shields.io/badge/status-403-red.svg
[status-404]: https://img.shields.io/badge/status-404-red.svg

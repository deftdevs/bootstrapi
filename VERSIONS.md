Version history
===============

## 0.1.0

With the release of Confluence 8.2.0, Atlassian has introduced an official backup and restore endpoint, enabling the export and import of spaces and entire sites via REST API.
This functionality aligns with what was previously provided by ConfAPI for Confluence.
In developing this new endpoint, Atlassian has completely redeveloped the internal Java codebase for handling space exports and imports, which was the foundation for ConfAPI's backup functionality.

Given these changes, maintaining ConfAPI's backup endpoint to provide identical functionality to Atlassian's official solution would require a complete redevelopment of our endpoint.
Consequently, we have decided to discontinue the ConfAPI backup endpoint.
Users seeking backup and restore capabilities are encouraged to transition to Atlassian's official endpoint:

* [Official Atlassian Confluence Backup and Restore REST API Endpoint](https://docs.atlassian.com/ConfluenceServer/rest/8.2.0/#api/backup-restore)

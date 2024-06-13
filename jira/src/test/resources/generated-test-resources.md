How generated test resources.zip has been generated
---------------------------------------------------

The whole steps don't have to be repeated when 'upgrading' the home zip,
they are just documented for reproducibility.

1. Start Jira with `./mvnw clean package jira:run` and login with 'admin:admin'
2. Change baseurl to localhost if necessary
3. Close health checks and other dialogs
4. Create user:
   ```
   username: user
   fullname: user
   email: user@example.com
   password: user
   ```
   with access to Jira Service Management and Jira Software (includes Jira Core)
5. Shut down using `[Ctrl]+[D]`
6. Save home ZIP with `./mvnw jira:create-home-zip` and copy
   `./target/jira/generated-test-resources.zip` to `src/test/resources/`

7. ...

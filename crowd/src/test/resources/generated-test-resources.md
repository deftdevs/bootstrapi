How generated test resources.zip has been generated
---------------------------------------------------

The whole steps don't have to be repeated when 'upgrading' the home zip,
they are just documented for replicability.

1. Start Crowd 5.2.2 with `mvn clean crowd:run` and login with 'admin:admin'
2. Change baseurl to localhost if necessary
3. Create group:
   ```
   name: crowd-users
   description: Crowd Users
   active: true
   ```
4. Create user:
   ```
   email: user@example.com
   username: user
   first name: user
   last name: user
   fullname: user
   password: user
   ```
5. Assign user 'user' to group 'crowd-users'
6. Allow group 'crowd-users' to authenticate in application 'crowd'
7. Shut down using `[Ctrl]+[D]`
8. Save home ZIP with `mvn crowd:create-home-zip` and copy
   `./target/crowd/generated-test-resources.zip` to `src/test/resources/`

9. ...

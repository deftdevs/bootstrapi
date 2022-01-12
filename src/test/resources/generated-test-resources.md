How generated test resources.zip has been generated
---------------------------------------------------

The whole steps don't have to be repeated when 'upgrading' the home zip,
they are just documented for replicability.

1. Start Crowd 4.4.0 with `./mvnw clean package crowd:run` and login with 'admin:admin'
2. Change baseurl to localhost if necessary
3. Create group:
   ```
   name: crowd-users
   description: Crowd Users
   active: true
   ```
4. Create user:
   ```
   username: user
   first name: user
   last name: user
   fullname: user
   email: user@example.com
   password: user
   ```
5. Assign user 'user' to group 'crowd-users'
6. Allow group 'crowd-users' to authenticate in application 'crowd'
7. Shut down using `[Ctrl]+[D]`
8. Save home ZIP with `./mvnw crowd:create-home-zip` and copy
   `./target/crowd/generated-test-resources.zip` to `src/test/resources/`

9. ...

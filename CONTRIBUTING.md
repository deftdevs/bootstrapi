# Contributing to BootstrAPI

Pull requests are welcome. This guide covers how to build and test the plugins and which conventions the code follows.

## Building and testing

The build requires Maven and a JDK matching the Atlassian products (Java 17 works well; the compiler release is set by the build).

- `mvn clean test` runs the unit tests of all modules.
- `mvn -pl <product> -am clean verify` (with `<product>` being `confluence`, `jira` or `crowd`) additionally starts the product via AMPS and runs the functional tests in the `it.*` packages against the live instance. This is the bar every change has to pass.
- The mail server functional tests expect a local [GreenMail](https://greenmail-mail-test.github.io/greenmail/) on the standard test ports, e.g. `docker run -d --name bootstrapi-greenmail -p 3025:3025 -p 3110:3110 greenmail/standalone`.
- `mvn -pl <product> -am <product>:run` starts a development instance for manual testing.
- `mvn package` regenerates the OpenAPI documentation (`<product>/README.md`, `Apis/`, `Models/`); commit the regenerated files together with the change that caused them.

## Conventions

### Naming and structure

| Concept | Convention | Example |
|---------|------------|---------|
| Model class | `EntityModel`, JSON field name `entity` | `MailServerSmtpModel`, `smtp` |
| Root element | The model's own name, from a `BootstrAPI` constant | `@XmlRootElement(name = BootstrAPI.MAIL_SERVER_SMTP)` |
| Collection endpoint | Plural resource and path | `DirectoriesResource`, `/directories` |
| Single-entity endpoint | Singular resource and path with ID | `DirectoryResource`, `/directory/{id}` |
| Group members | Class names carry the hierarchy | `SettingsBrandingBannerModel` |
| Getters/setters | Carry the full hierarchy | `getSettingsBrandingBanner()` |

The configuration structure is congruent everywhere: JSON field paths, status-map keys and endpoint paths line up one-to-one (`settings.branding.colorScheme` ↔ status key `settings/branding/colorScheme` ↔ `/settings/branding/color-scheme`). URL segments are kebab-case, JSON fields camelCase; both come from constants in `BootstrAPI`.

### Models

Models are Lombok beans annotated with `@Data`:

- Leaf models use `@Builder`, `@NoArgsConstructor` and `@AllArgsConstructor`; production code creates instances through builders only.
- Models in an inheritance hierarchy use `@SuperBuilder` and `@EqualsAndHashCode(callSuper = true)` instead.
- A sub-model declares the model containing it with `@SubEntityOf`; field names and status keys are derived reflectively via `FieldNames`. Never spell out a status key or field name as a string literal.
- Models provide `EXAMPLE_*` constants for tests.
- Secret fields (passwords, license keys) are write-only: they are accepted in requests but never echoed in responses.

### REST resources

- The resource interface defines the API including all `javax.ws.rs` and OpenAPI annotations; only `@Path` for the class itself belongs on the implementation. Exception: when the response schema is a product-specific model (the `_all` and composite `/settings` endpoints), the `@Operation` annotations live on the product implementation, since interface-level copies would be shadowed.
- Implementations are annotated with `@SystemAdminOnly` and use `@Inject` constructor injection.
- Endpoints consume and produce JSON and YAML; list `MediaType.APPLICATION_JSON` first so JSON stays the default response format.
- Do not catch exceptions in resources. Throw the exceptions from `commons.exception.web`; the registered `ExceptionMapper`s translate them into `ErrorCollection` responses. Unexpected exceptions must not leak internal details to the client.

### Services

- Composite services apply each present sub-field independently with the `ServiceResultUtil` helpers and report a per-field status entry; absent sub-fields are skipped.
- The overall response code of a composite is the highest sub-field status code (200 only if everything succeeded).

### Tests

- Every endpoint change needs unit tests and functional tests. Functional tests live in the `it.*` packages of the affected products and run against a real instance during `verify`.
- Expected status codes are asserted as plain integer literals (`assertEquals(200, ...)`).
- Test fixtures reuse the models' `EXAMPLE_*` constants and derive keys via `FieldNames` instead of repeating literals.

## Pull requests

- Keep pull requests focused on one topic.
- Make sure `mvn -pl <product> -am clean verify` is green for every affected product before asking for review.
- Include the regenerated OpenAPI documentation when the API surface changed.

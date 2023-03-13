Example code for Contact Manager synchronization webhook extension.

# contact-manager-integration-webhook-sync-extension

No-op implementation of Contact Manager synchronization webhook that listens for synchronization requests on
`http://localhost:<port>/service-api/v1/webhooks/contacts/sync` endpoint, validates them and logs requested
actions while actually skipping real Core Banking System synchronization.

#Getting Started
* [Extend and build](https://community.backbase.com/documentation/ServiceSDK/latest/extend_and_build)

## Dependencies

Requires a running Eureka registry, by default on port 8080.

## Configuration

Service configuration is under `src/main/resources/application.yaml`.

## Running

To run the service in development mode, use:
- `mvn spring-boot:run`

To run the service from the built binaries, use:
- `java -jar target/contact-manager-integration-webhook-sync-extension-<version>.jar`

## Authorization

This service uses service-2-service authentication on its receiving endpoints.

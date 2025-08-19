# Spring Boot Crash Course API

This project now includes OpenAPI 3 documentation generated via springdoc-openapi.

## API Documentation

- JSON Spec: `http://localhost:3000/v3/api-docs`
- Swagger UI: `http://localhost:3000/swagger-ui/index.html`

## Authentication

JWT bearer authentication is defined with the `bearerAuth` security scheme. Supply the access token in the `Authorization` header:

```
Authorization: Bearer <access_token>
```

A refresh token workflow is implemented. Use the refresh token to obtain a new access/refresh pair at the appropriate endpoint (to be implemented at controller level).

## Development

Run the application:

```bash
./gradlew bootRun
```

Then open the Swagger UI URL above in a browser.


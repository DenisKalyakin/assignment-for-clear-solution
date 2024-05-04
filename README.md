#### Java practical test assignment
# RESTful API for User Management

This project implements a RESTful API for managing users. It is built using Spring Boot and follows best practices for RESTful API design.

## Introduction

This project aims to provide a comprehensive RESTful API for user management. It includes endpoints for creating, updating, deleting users, as well as searching users by birth date range.

## Requirements

The requirements for this project include:

- Implementation of a RESTful API for user management.
- Validation for user fields such as email pattern, age, and birthdate range.
- Search for users by birthdate range.
- Unit tests using Spring. 
- Error handling for REST. 
- Responses in JSON format. 
- Use of Spring Boot. 
- Optional: Use database for data persistence.

[task reference](https://docs.google.com/document/d/1LosRgr72sJYcNumbZKET7uiIJ3Un_ORl25Psn1Dd9hw/edit)

## Implementation Details

The API is implemented using Spring Boot, following RESTful API best practices. It includes controllers for user management, services for business logic, and repositories for data access. Unit tests are written using Spring's testing framework to ensure code coverage and reliability.

## Running the Application

To run the application, follow these steps:

1. Clone the repository.
2. Open the project in your preferred IDE.
3. Build the project using Maven.
4. Run the application as a Spring Boot application.

## Endpoints

The following endpoints are available:

- `POST /auth/register`: Create/registration a new user.
- `PATCH /users/{id}/name`: Update a user's firstname and lastname.
- `PATCH /users/{id}/address`: Update a user's address.
- `PATCH /users/{id}/phone`: Update a user's phone number.
- `PUT /users/{id}`: Update a user's fields without address.
- `DELETE /users/{id}`: Delete (soft delete) a user.
- `GET /users/birth-range`: Search for users by birthdate range.

## Testing

Unit and integration tests are written to cover all implemented functionality. These tests ensure the reliability and correctness of the codebase.

## Error Handling

Error handling is implemented to provide meaningful error responses for RESTful API requests. Exceptions are caught and appropriate HTTP status codes and error messages are returned to the client.

## JSON Format

API responses are formatted in JSON to ensure compatibility with modern web applications and ease of parsing.

## Technologies Used

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- H2 Database
- Liquibase
- Lombok
- Mockito
- Maven

## On completion

This project was implemented by Denis Kaliakin. I want to thank you for the opportunity to implement this task.
 
#### email: dev.denis.kaliakin@gmail.com



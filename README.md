# Flight-Booking-Engine-Project

# Flight Booking Engine Project

## Project Overview

The Flight Booking Engine is a Spring Boot-based backend application designed to simulate an airline reservation system. The primary objective of the project is to demonstrate transaction management and database consistency using Spring Data JPA and the H2 in-memory database.

In a real-world airline booking system, multiple operations must occur together to complete a reservation successfully. When a passenger books a flight:

1. The available seat count must be reduced.
2. The payment transaction must be recorded.

If either operation fails, the entire booking process must be reversed automatically. This project implements atomic transactions to ensure that both operations either succeed together or fail together, preserving data integrity.

## Business Problem

Consider the following scenario:

* A passenger books the last available seat.
* The system deducts the seat from inventory.
* Immediately afterward, the payment transaction fails.

Without transaction management, the seat would remain unavailable even though the booking was not completed. This results in inconsistent data and potential revenue loss.

To solve this problem, the application uses Spring's `@Transactional` support to guarantee Atomicity, one of the ACID properties of database systems.

## Core Features

### Flight Management

* Store flight information.
* Track available seat inventory.
* Retrieve flight details by ID.
* Prevent bookings when no seats are available.

### Payment Processing

* Record payment transactions.
* Associate payments with flights.
* Validate payment limits before confirming bookings.

### Transaction Management

* Deduct seats and save payment information as a single transaction.
* Automatically rollback all changes when an exception occurs.
* Maintain database consistency under failure conditions.

## Architecture

The application follows a layered architecture pattern:

### Entity Layer

Represents database tables and business objects.

#### Flight

Stores:

* Flight ID
* Flight Number
* Destination
* Available Seats

#### PaymentInfo

Stores:

* Payment ID
* Flight ID
* Passenger Name
* Payment Amount

### Repository Layer

Provides database access through Spring Data JPA repositories.

* FlightRepository
* PaymentRepository

These repositories inherit standard CRUD operations from `JpaRepository`.

### Service Layer

Contains business logic and transaction management.

Responsibilities:

* Validate flight existence.
* Check seat availability.
* Reduce seat count.
* Validate payment amount.
* Save payment records.
* Trigger rollback when validation fails.

### Controller Layer

Exposes REST APIs for:

* Retrieving flight information.
* Booking flights.
* Handling success and failure responses.

## Transaction Workflow

### Successful Booking

```text
Passenger Request
        ↓
Find Flight
        ↓
Seat Available?
        ↓
Decrease Seat Count
        ↓
Validate Payment
        ↓
Save Payment
        ↓
Commit Transaction
        ↓
Booking Successful
```

### Failed Booking (Rollback)

```text
Passenger Request
        ↓
Find Flight
        ↓
Seat Available?
        ↓
Decrease Seat Count
        ↓
Payment > $500 ?
        ↓
Exception Thrown
        ↓
Rollback Transaction
        ↓
Seat Count Restored
```

## Business Rules

### Rule 1: Flight Must Exist

A booking cannot proceed if the provided flight ID does not exist.

### Rule 2: Seat Availability

A booking cannot proceed when available seats are zero.

### Rule 3: Payment Limit

Payments greater than $500 are considered invalid.

```java
if(paymentInfo.getAmount() > 500){
    throw new RuntimeException("Payment exceeds limit");
}
```

This exception is intentionally used to verify that transaction rollback is functioning correctly.

## REST API Endpoints

### Get Flight Details

**GET**

```http
/api/{id}
```

Returns the flight details for the given ID.

### Book Flight

**POST**

```http
/api/book
```

Accepts a PaymentInfo JSON payload.

Example:

```json
{
  "flightId": 1,
  "passengerName": "John Doe",
  "amount": 300
}
```

Successful bookings return:

```http
200 OK
```

Failed bookings return:

```http
500 Internal Server Error
```

## Preloaded Test Data

The application initializes the database with a sample flight:

| ID | Flight Number | Destination | Available Seats |
| -- | ------------- | ----------- | --------------- |
| 1  | CC-101        | Bengaluru   | 10              |

This allows immediate testing of booking and rollback functionality.

## Technologies Used

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Lombok
* Maven

## Learning Objectives

By completing this project, developers will gain practical experience with:

* Spring Boot REST APIs
* JPA Entity Mapping
* Spring Data Repositories
* Constructor-Based Dependency Injection
* Transaction Management using `@Transactional`
* ACID Properties of Databases
* Automatic Rollback Handling
* Exception-Based Business Validation
* Layered Application Architecture

## Expected Outcome

Upon completion, the Flight Booking Engine will function as a reliable airline reservation backend capable of maintaining data integrity during booking operations. Through transaction management, the system guarantees that seat inventory and payment records remain synchronized, ensuring that no partial or inconsistent bookings can occur.

# Personal Finance Tracker – Backend

A Spring Boot REST API backend for a full-stack Personal Finance Tracker application that helps users manage income, expenses, monthly budgets, and financial summaries.

## Project Overview

The Personal Finance Tracker provides a centralized platform for recording and analyzing personal financial activities.

The backend handles authentication, business logic, database operations, budget calculations, and financial summary generation.

## Key Features

- User Registration and Login
- JWT-based Authentication
- BCrypt Password Encryption
- Income Management
- Expense Management
- Monthly Budget Management
- Budget Status Tracking
- Budget Exceeded Detection
- Monthly Financial Summary
- Income vs Expense Analysis
- MySQL Database Integration
- RESTful APIs
- Global Exception Handling

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot 3.4.8 | REST API development |
| Spring Data JPA | Database interaction |
| Hibernate | Object-relational mapping |
| Spring Security | Authentication and security |
| JWT | Token-based authentication |
| BCrypt | Password hashing |
| MySQL 8 | Database |
| Maven | Build and dependency management |

## Application Architecture

```text
Angular Frontend
       |
       | REST API
       ↓
Spring Boot Backend
       |
       ├── Controllers
       ├── Security / JWT
       ├── Business Logic
       └── Repositories
              |
              ↓
       Spring Data JPA / Hibernate
              |
              ↓
          MySQL Database
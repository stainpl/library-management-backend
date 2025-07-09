# 📚 Library Management API (Spring Boot)

A full-stack CRUD Library Management System built with Spring Boot, JPA, and MySQL.

## 🚀 Features
- Add / Delete / View Books
- Create / Delete / List Users
- Borrow & Return Books
- RESTful API for client integration
- MySQL for persistence

## 📦 Tech Stack
- Java 21
- Spring Boot 3.5.3
- JPA / Hibernate
- MySQL
- Maven

## 🧑‍💻 API Endpoints

### 📘 Books
| Method | Endpoint             | Description            |
|--------|----------------------|------------------------|
| POST   | `/api/books`         | Add a new book         |
| GET    | `/api/books`         | Get all books          |
| GET    | `/api/books/{id}`    | Get book by ID         |
| DELETE | `/api/books/{id}`    | Delete a book          |

### 👤 Users
| Method | Endpoint             | Description             |
|--------|----------------------|-------------------------|
| POST   | `/api/users`         | Create a new user       |
| GET    | `/api/users`         | List all users          |
| DELETE | `/api/users/{id}`    | Delete a user (also clears borrowed books) |

### 📚 Borrowing
| Method | Endpoint                        | Description              |
|--------|----------------------------------|--------------------------|
| POST   | `/api/borrow?userId=&bookId=`    | Borrow a book            |
| POST   | `/api/borrow/return?bookId=`     | Return a book            |
| GET    | `/api/borrow/active`             | View all active borrows  |

## ⚙️ Setup

1. Clone repo
2. Create MySQL DB `librarydb`
3. Configure `application.properties`
4. Run the app:
```bash
./mvnw spring-boot:run

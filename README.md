# BookStore Application

A Spring Boot-based web application for publishing and reviewing books. This application provides a RESTful API for managing books, users, and reviews.

## Prerequisites

Before you begin, ensure you have the following installed:
- Java 21 or higher
- Maven
- MySQL Server
- Git

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/MahakAgrawal02/BookPublishing-ReviewSystem
cd BookStore
```

### 2. Database Setup

1. Create a MySQL database named `bookreview_db`
2. Update the database configuration in `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookreview_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Features

- User authentication and authorization using JWT
- Book management (CRUD operations)
- Review system for books
- RESTful API endpoints
- Data validation
- Security features

## Technologies Used

- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- MySQL
- Maven
- Lombok
- JWT for authentication
- Swagger/OpenAPI for documentation

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── bookstore/
│   │           ├── BookStoreApplication.java
│   │           ├── configs/
│   │           ├── controllers/
│   │           ├── entity/
│   │           ├── exceptionHandlers/
│   │           ├── exceptions/
│   │           ├── helpers/
│   │           ├── payload/
│   │           ├── repository/
│   │           ├── security/
│   │           ├── services/
│   │           └── utils/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── bookstore/
                ├── BookStoreApplicationTests.java
                ├── controllers/
                └── services/
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

If you encounter any issues or have questions, please open an issue in the GitHub repository. 
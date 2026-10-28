# Bank Management System

A console-based banking application developed using Core Java, JDBC, and MySQL to manage basic banking operations.

## Features

- Create a new bank account
- Deposit money into an account
- Withdraw money from an account
- Check account balance
- View transaction history
- Store account and transaction data in MySQL

## Technologies Used

- **Java** — Core Java and Object-Oriented Programming (OOP)
- **JDBC** — Database connectivity
- **MySQL** — Relational database
- **Maven** — Dependency management
- **IntelliJ IDEA** — Development environment

## Project Structure

```text
BankManagementSystem/
├── src/main/java/com/bank/
│   ├── Account.java
│   ├── AccountDAO.java
│   ├── DBConnection.java
│   └── Main.java
├── pom.xml
├── .gitignore
└── README.md
```

## Database Setup

1. Install and start MySQL.
2. Create the `bank_management` database.
3. Create the required account and transaction tables.
4. Configure your MySQL connection details in `DBConnection.java`.

Ensure your database username and password are configured locally and are not committed to GitHub.

## How to Run

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Configure the MySQL database and connection details.
4. Reload the Maven project dependencies.
5. Run `Main.java`.

## Concepts Practiced

- Object-Oriented Programming (OOP)
- Classes and objects
- JDBC database connectivity
- SQL queries and relational database operations
- Data Access Object (DAO) design pattern
- Exception handling

## Author

**Bhavesh Patil**

GitHub: [Bhavesh451](https://github.com/Bhavesh451)

---

*Developed as a Java and database management project for learning and placement preparation.*

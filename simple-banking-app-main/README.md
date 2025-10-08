Simple Banking Application (CLI)

A lightweight Command-Line Banking System built using Java and Maven, designed to simulate real-world banking operations such as account management, transactions, and statements — all within the terminal.

It also includes a full TestNG test suite to validate business logic and ensure application reliability.

🚀 **Features**

Customer Management – Create and view customers

Account Management – Open Savings and Checking accounts

Transactions – Deposit, Withdraw, and Transfer funds

Bank Statements – Generate transaction statements within a date range

Automated Testing – Comprehensive TestNG test coverage

**Project Structure**
simple-banking-app/
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/bank/
│   │   │       ├── BankingApp.java
│   │   │       ├── exception/
│   │   │       │   ├── AccountNotFoundException.java
│   │   │       │   ├── DuplicateAccountException.java
│   │   │       │   ├── InsufficientFundsException.java
│   │   │       │   └── InvalidAmountException.java
│   │   │       ├── model/
│   │   │       │   ├── Account.java
│   │   │       │   ├── Customer.java
│   │   │       │   └── Transaction.java
│   │   │       ├── repository/
│   │   │       │   ├── AccountRepository.java
│   │   │       │   └── TransactionRepository.java
│   │   │       └── service/
│   │   │           ├── AccountService.java
│   │   │           ├── BankStatementService.java
│   │   │           ├── CustomerService.java
│   │   │           └── TransactionService.java
│   └── test
│       └── java/com/bank/
│           ├── repository/AccountRepositoryTest.java
│           └── service/
│               ├── AccountServiceTest.java
│               ├── BankStatementServiceTest.java
│               └── TransactionServiceTest.java

**Requirements**

Java 17 or higher

Maven 3.9 or higher

To verify your setup:

java -version
mvn -version

**Running the Application**

Open the terminal in the project folder.

Run the following command:

mvn exec:java


**A menu will appear:**

1) Create Customer  
2) Create Account  
3) Deposit  
4) Withdraw  
5) Transfer  
6) Statement  
7) Delete Account  
8) View Customer  
9) Exit  


**Select an option (1–9) and follow the on-screen prompts.**

**Example Usage**

Create a customer → enter ID, name, email, phone

Create an account → choose SAVINGS or CHECKING and a starting balance

Deposit or withdraw money

Transfer between accounts

Generate statements between two dates (yyyy-MM-dd HH:mm:ss)

Exit the program (option 9)

**Business Rules**

One customer can hold up to two accounts (1 Savings + 1 Checking)

Transaction amounts must be greater than zero

Transfers between the same account are not allowed

Insufficient balance triggers an exception

Statement date range cannot exceed 6 months

**Running Tests**

Run all tests:

mvn test


Run only transaction-related tests:

mvn test -Dgroups=transactions


Run a specific test class:

mvn -Dtest=TransactionServiceTest test

**Test Coverage Highlights**

Deposit, withdrawal, and transfer operations

Balance accuracy after each operation

Exception handling for invalid inputs and insufficient funds

Account validation and statement generation

<img width="294" height="543" alt="image" src="https://github.com/user-attachments/assets/6e500f33-61a6-47b2-923c-5e123135c8a3" />


Repositories Layer
------------------
AccountRepository
  ↳ manages Account storage/retrieval
TransactionRepository
  ↳ manages Transaction records

Service Layer
-------------
AccountService
  ↳ deposit(), withdraw(), transfer()

CustomerService
  ↳ createCustomer(), viewCustomer()

TransactionService
  ↳ recordTransaction()

BankStatementService
  ↳ generateStatement(fromDate, toDate)

Main App
--------
BankingApp
  ↳ CLI Menu → Calls appropriate services

🔄 System Flow Diagram (CLI Workflow)
                ┌────────────────────────┐
                │       User (CLI)       │
                └────────────┬───────────┘
                             │
                             ▼
                ┌────────────────────────┐
                │      BankingApp        │
                │ (Handles Menu Input)   │
                └────────────┬───────────┘
                             │
                ┌────────────┼────────────┐
                ▼             ▼            ▼
     ┌────────────────┐  ┌──────────────┐  ┌─────────────────┐
     │CustomerService │  │AccountService│  │TransactionService│
     └────────────────┘  └──────────────┘  └─────────────────┘
             │                  │                   │
             ▼                  ▼                   ▼
     ┌────────────────┐  ┌──────────────┐  ┌─────────────────┐
     │ Customer Model │  │ Account Model│  │ Transaction Model│
     └────────────────┘  └──────────────┘  └─────────────────┘
             │                  │                   │
             ▼                  ▼                   ▼
     ┌────────────────┐  ┌──────────────┐  ┌─────────────────┐
     │ Customer Repo  │  │ Account Repo │  │ Transaction Repo │
     └────────────────┘  └──────────────┘  └─────────────────┘
                             │
                             ▼
                ┌────────────────────────┐
                │   BankStatementService │
                │  (Generates Statement) │
                └────────────────────────┘

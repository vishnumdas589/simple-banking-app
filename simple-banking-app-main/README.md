
# Simple Banking Application (CLI)

This project is a simple banking system made using **Java** and **Maven**.  
It runs in the **command line** and allows you to:

- Create customers  
- Create accounts (Savings / Checking)  
- Deposit money  
- Withdraw money  
- Transfer money between accounts  
- View account statements  

It also has a complete **TestNG test suite** to check that everything works correctly.

Project Structure
simple-banking-app/
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── bank
│   │   │           └── simple_banking_app
│   │   │               ├── BankingApp.java
│   │   │               ├── exception
│   │   │               │   ├── AccountNotFoundException.java
│   │   │               │   ├── DuplicateAccountException.java
│   │   │               │   ├── InsufficientFundsException.java
│   │   │               │   └── InvalidAmountException.java
│   │   │               ├── model
│   │   │               │   ├── Account.java
│   │   │               │   ├── Customer.java
│   │   │               │   └── Transaction.java
│   │   │               ├── repository
│   │   │               │   ├── AccountRepository.java
│   │   │               │   └── TransactionRepository.java
│   │   │               └── service
│   │   │                   ├── AccountService.java
│   │   │                   ├── BankStatementService.java
│   │   │                   ├── CustomerService.java
│   │   │                   └── TransactionService.java
│   └── test
│       └── java
│           └── com
│               └── bank
│                   └── simple_banking_app
│                       ├── repository
│                       │   └── AccountRepositoryTest.java
│                       └── service
│                           ├── AccountServiceTest.java
│                           ├── BankStatementServiceTest.java
│                           └── TransactionServiceTest.java




## Requirements

- Java 17 or later  
- Maven 3.9 or later  

To check if they are installed, run these commands in terminal:  
`java -version`  
`mvn -version`

## How to Run the Application

1. Open a terminal in the project folder  
2. Run: `mvn exec:java`  
3. You will see a menu like this:

1) Create Customer  
2) Create Account  
3) Deposit  
4) Withdraw  
5) Transfer  
6) Statement  
7) Delete Account  
8) View Customer  
9) Exit  

Type a number (1-9) and follow the instructions on the screen.

## Example Usage

- First, choose option 1 to create a customer (enter ID, name, email, phone)  
- Then choose option 2 to create an account (choose SAVINGS or CHECKING and set a starting amount)  
- Use option 3 to deposit money  
- Use option 4 to withdraw money  
- Use option 5 to transfer money to another account  
- Use option 6 to generate a statement (enter two dates in format `yyyy-MM-dd HH:mm:ss`)  
- Choose option 9 to exit the program

## Business Rules

- Each customer can have at most **two accounts** (one SAVINGS + one CHECKING)  
- Deposit, withdraw, and transfer amounts must be **greater than zero**  
- Withdraw and transfer will fail if there is **not enough balance**  
- Transfer to the **same account** is not allowed  
- Statement dates must be entered manually and cannot be older than 6 months  

## How to Run Tests

This project uses **TestNG** to test all features.

- Run all tests:  
`mvn test`

- Run only transaction tests:  
`mvn test -Dgroups=transactions`

- Run a specific test class:  
`mvn -Dtest=TransactionServiceTest test`

## What the Tests Check

- Deposit adds money correctly  
- Withdraw subtracts money correctly  
- Transfer updates both accounts together  
- Exceptions are thrown for invalid amounts, insufficient balance, or wrong account numbers  
- Balances remain correct after every operation  
- Statement shows transactions correctly  


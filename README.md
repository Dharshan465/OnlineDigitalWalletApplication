# Online Digital Wallet Application (Layered Architecture with Spring Boot, Spring Data JPA(Custom methods , Validation), and H2 Database)

---

This project implements an Online Digital Wallet Application using a layered architecture with Spring Boot, Spring Data JPA , and H2 Database. The application allows users to register, add funds, withdraw funds, transfer funds, view all wallets, and delete wallets.

---

### Project Structure:
```
wallet/
   ├── model
   │    └── Wallet.java
   ├── exception    
   │    ├── WalletAddFundException.java
   │    ├── WalletException.java
   │    ├── WalletNotFoundException.java
   │    ├── WalletTransferFundException.java
   │    ├── WalletWithdrawFundException.java
   │    ├──GlobalExceptionHandler.java
   │    └── ErrorResponse.java
   ├── service
   │    ├── WalletService.java
   │    └── WalletServiceImplementation.java
   ├── repository
   │    └── WalletRepository.java
   ├── controller
   │    └── WalletController.java 
   └── OnlineDigitalWalletApplication.java

```

---

### Key Components:

#### WalletController.java
- Handles HTTP requests and responses.
- Defines endpoints for wallet operations.
- Uses `@RestController` and `@RequestMapping("api/v1/wallet")` annotations.
- Injects `WalletService` using `@Autowired`.
- In WalletServiceImplementation, injects WalletDao using @Autowired.
- Endpoints:
    - `POST` : Register a new wallet.
    - `GET /allWallets`: Get all wallets.
    - `GET /{emailId}`: Get a wallet by email.
    - `GET /id/{WalletId}`: Get a wallet by ID.
    - `PUT /{emailId}`:Updates wallet details.
    - `PATCH /addFunds/{emailId}/{amount}`: Add funds to a wallet.
    - `PATCH /withdrawFunds/{emailId}/{amount}`: Withdraw funds from a wallet.
    - `PATCH /transferFunds/{fromEmailId}/{toEmailId}/{amount}`: Transfer funds between wallets.
    - `DELETE /{email}`: Delete a wallet by email.

#### ErrorResponse.java
- A simple POJO to represent error responses.
- Contains fields for timestamp, status, error, message, and path.


#### GlobalExceptionHandler.java
- Handles custom exceptions and returns appropriate HTTP responses.
- Uses `@ControllerAdvice` and `@ExceptionHandler` annotations.
- Custom exceptions handled:
    - `WalletNotFoundException`
    - `WalletAddFundException`
    - `WalletWithdrawFundException`
    - `WalletTransferFundException`
    - `WalletException`
- Returns `ResponseEntity<Object>` with error messages and HTTP status codes.


#### Service Methods:
- registerNewUserWallet
- addFundsToWalletByEmailID
- withdrawFundsFromWalletByEmailID
- getAllCustomerWallets
- deleteWalletByEmailID
- transferFunds
- getWalletByEmailID
- getWalletByID
- updateWalletByEmailID

#### JPA Repository Methods:
- save (in-built)
- findById (in-built)
- findAll (in-built)
- deleteById (in-built)
- findByEmail (custom method)
- findByBalanceBetween (custom method)
- findByEmailContainingIgnoreCase (custom method)
- findByBalance(custom method)


#### H2 Database:
- TABLE WALLET
- COLUMNS: id, name, email, password, balance

## GitHub Repository: 

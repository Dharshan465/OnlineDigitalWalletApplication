# Online Digital Wallet Application (Layered Architecture with Spring Boot, Spring Data JPA(Custom methods , Validation), and H2 Database)

---

This project implements an Online Digital Wallet Application using a layered architecture with Spring Boot, Spring Data JPA , and H2 Database. The application allows users to register, add funds, withdraw funds, transfer funds, view all wallets, and delete wallets.

Assignment 16 : Online Digital Wallet Application (Layered Architecture with Spring Boot, Spring Data JPA(Custom methods , Validation), and H2 Database)
Assignment 20: Online Digital Wallet Application - Junit Test Cases for Service Layer

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

test/java/ford
   └── WalletServiceTests.java

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

# WalletService Test Cases Summary

Overview of the test cases implemented for the `WalletService` within the `WalletServiceTests.java` file. Each section corresponds to a method in the `WalletService` interface, detailing the positive and negative scenarios covered by the tests.

---

### 1. `registerNewUserWallet(Wallet newWallet)`

*   **`newWalletRegistrationTest()`**:
  *   **Positive:** Verifies successful registration of a new wallet, ensuring all provided details are correctly stored and a wallet ID is generated.
*   **`checkForIdAlreadyExistsExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletException` is thrown when attempting to register a wallet with an email ID that already exists, confirming the duplicate email handling.

### 2. `deleteWalletByEmailId(String emailId)`

*   **`deleteWalletByEmailIdTest()`**:
  *   **Positive:** Confirms that a wallet can be successfully deleted by its email ID, and subsequent attempts to retrieve it result in a `WalletNotFoundException`.
*   **`deleteNonExistentWalletShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when attempting to delete a wallet using a non-existent email ID.

### 3. `addFundsToWalletByEmailId(String emailId, Double amount)`

*   **`addFundsTest()`**:
  *   **Positive:** Verifies that funds are correctly added to an existing wallet, resulting in the expected updated balance.
*   **`addNegativeAmountShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletAddFundException` is thrown when attempting to add a negative amount of funds.

### 4. `withdrawFundsFromWalletByEmailId(String emailId, Double amount)`

*   **`withdrawFundsFromWalletByEmailIdTest()`**:
  *   **Positive:** Confirms that funds are successfully withdrawn from an existing wallet, and the balance is updated as expected.
*   **`withdrawInsufficientFundsShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletWithdrawFundException` is thrown when attempting to withdraw an amount greater than the available balance.
*   **`withdrawFromNonExistentWalletShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when attempting to withdraw funds from a wallet that does not exist.
*   **`withdrawNegativeAmountShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletWithdrawFundException` is thrown when attempting to withdraw a negative amount of funds.

### 5. `transferFunds(String fromEmailId, String toEmailId, Double amount)`

*   **`transferFundsTest()`**:
  *   **Positive:** Verifies the successful transfer of funds between two wallets, confirming that both sender's and receiver's balances are updated accurately.
*   **`transferFundsInsufficientBalanceTest()`**:
  *   **Negative:** Asserts that a `WalletTransferFundException` is thrown when the sender's wallet has insufficient funds for the transfer.
*   **`transferFundsNonExistentSenderTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when the sender's email ID does not correspond to an existing wallet.
*   **`transferFundsNonExistentReceiverTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when the receiver's email ID does not correspond to an existing wallet.
*   **`transferNegativeAmountShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletTransferFundException` is thrown when attempting to transfer a negative amount of funds.

### 6. `getAllCustomerWallets()`

*   **`getAllCustomerWalletsTest()`**:
  *   **Positive:** Verifies that a collection containing all registered customer wallets is returned, and its size matches the expected count.
*   **`getAllCustomerWalletsWhenNoneExistTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when there are no wallets in the system to retrieve.

### 7. `getWalletByEmailId(String emailId)`

*   **`getWalletByEmailIdTest()`**:
  *   **Positive:** Confirms that a wallet can be successfully retrieved using its unique email ID.
*   **`getNonExistentWalletByEmailIdShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when attempting to retrieve a wallet with a non-existent email ID.

### 8. `getWalletById(Integer walletid)`

*   **`getWalletByIdTest()`**:
  *   **Positive:** Confirms that a wallet can be successfully retrieved using its unique integer ID.
*   **`getNonExistentWalletByIdShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when attempting to retrieve a wallet with a non-existent ID.

### 9. `updateWalletByEmailId(String emailId, Wallet newWallet)`

*   **`updateWalletByEmailIdTest()`**:
  *   **Positive:** Verifies that an existing wallet's details (e.g., customer name, password, balance) can be successfully updated using its email ID.
*   **`updateNonExistentWalletShouldThrowExceptionTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when attempting to update a wallet with a non-existent email ID.

### 10. `getWalletsByBalanceRange(Double min, Double max)`

*   **`getWalletsByBalanceRangeTest()`**:
  *   **Positive:** Verifies that a collection of wallets whose balances fall within a specified minimum and maximum range is returned correctly.
*   **`getWalletsByBalanceRangeNoMatchTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when no wallets are found within the specified balance range.

### 11. `getWalletsByExactBalance(Double balance)`

*   **`getWalletsByExactBalanceTest()`**:
  *   **Positive:** Verifies that a collection of wallets matching an exact specified balance is returned correctly.
*   **`getWalletsByExactBalanceNoMatchTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when no wallets are found with the exact specified balance.

### 12. `getWalletsByEmailDomain(String domain)`

*   **`getWalletsByEmailDomainTest()`**:
  *   **Positive:** Verifies that a collection of wallets whose email addresses belong to a specific domain is returned correctly.
*   **`getWalletsByEmailDomainNoMatchTest()`**:
  *   **Negative:** Asserts that a `WalletNotFoundException` is thrown when no wallets are found for the specified email domain.

## GitHub Repository: https://github.com/Dharshan465/OnlineDigitalWalletApplication.git
## Github Repository (test):https://github.com/Dharshan465/OnlineDigitalWalletApplication/blob/main/src/test/java/ford/assignment16/OnlineDigitalWalletApplication/WalletServiceTests.java
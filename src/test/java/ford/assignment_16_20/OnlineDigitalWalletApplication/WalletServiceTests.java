package ford.assignment_16_20.OnlineDigitalWalletApplication;

import ford.assignment_16_20.OnlineDigitalWalletApplication.Repository.WalletRepository;
import ford.assignment16.OnlineDigitalWalletApplication.exception.*;
import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.*;
import ford.assignment_16_20.OnlineDigitalWalletApplication.model.Wallet;
import ford.assignment_16_20.OnlineDigitalWalletApplication.service.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class WalletServiceTests {

    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;

    private Wallet wallet;

    @BeforeEach
    void setUpTestDataBeforeEachTest() {
        this.walletRepository.deleteAll();

        this.wallet = new Wallet(null, "MyName", "name@gmail.com", "123My@Name", 5000.0);

    }

    //Integration tests for WalletService

    //Positive test case for creating a new wallet

    @Test
    void newWalletRegistrationTest() throws WalletException {
        assumeTrue(walletService != null, "Wallet service bean is not available");

        Wallet createdWallet = this.walletService.registerNewUserWallet(wallet);
        assertNotNull(createdWallet);
        assertNotNull(createdWallet.getWalletId());
        assertEquals("MyName", createdWallet.getCustomerName());
        assertEquals("name@gmail.com", createdWallet.getEmail());
        assertEquals("123My@Name", createdWallet.getPassword());
        assertEquals(5000.0, createdWallet.getBalance());
    }
// Negative test case for creating a wallet with duplicate email
    @Test
    @DisplayName("Checking for duplicate id while creating wallet")
    void checkForIdAlreadyExistsExceptionTest() throws WalletException {
        this.walletService.registerNewUserWallet(wallet);

        Wallet duplicateWallet = new Wallet(null, "AnotherName", "name@gmail.com", "password2", 1000.0);

        WalletException walletException = assertThrows(WalletException.class,
                () -> this.walletService.registerNewUserWallet(duplicateWallet));

        assertEquals("User already exists with emailId: name@gmail.com", walletException.getMessage());
    }
    // Positive test case for adding funds to wallet
    @Test
    void addFundsTest() throws WalletException, WalletAddFundException, WalletNotFoundException {
        Wallet registeredWallet = this.walletService.registerNewUserWallet(wallet);

        this.walletService.addFundsToWalletByEmailId(registeredWallet.getEmail(), 500.0);

        Wallet foundWallet = this.walletService.getWalletByEmailId(registeredWallet.getEmail());

        assertEquals(5500.0, foundWallet.getBalance());
    }
    // Negative test case for adding negative amount to wallet

    @Test
    @DisplayName("Adding negative amount to wallet should throw exception")
    void addNegativeAmountShouldThrowExceptionTest() throws WalletException {
        this.walletService.registerNewUserWallet(wallet);

        WalletAddFundException walletAddFundException = assertThrows(WalletAddFundException.class,
                () -> this.walletService.addFundsToWalletByEmailId(wallet.getEmail(), -100.0));

        assertEquals("Amount is less than 0, cannot add funds", walletAddFundException.getMessage());
    }

    //Positive test case for deleting a wallet

    @Test
    void deleteWalletByEmailIdTest() throws WalletException, WalletNotFoundException {
        this.walletService.registerNewUserWallet(wallet);
        Wallet deletedWallet = this.walletService.deleteWalletByEmailId(wallet.getEmail());
        assertNotNull(deletedWallet);
        assertEquals(wallet.getEmail(), deletedWallet.getEmail());
        assertThrows(WalletNotFoundException.class, () -> this.walletService.getWalletByEmailId(wallet.getEmail()));
    }

    @Test
    @DisplayName("Deleting a non-existent wallet should throw exception")
    void deleteNonExistentWalletShouldThrowExceptionTest() {
        assertThrows(WalletNotFoundException.class, () -> this.walletService.deleteWalletByEmailId("nonexistent@gmail.com"));
    }
    // Positive test case for withdrawing funds from wallet

    @Test
    void withdrawFundsFromWalletByEmailIdTest() throws WalletException, WalletWithdrawFundException, WalletNotFoundException {
        // Test for successfully withdrawing funds from a wallet.
        Wallet registeredWallet = this.walletService.registerNewUserWallet(wallet);

        this.walletService.withdrawFundsFromWalletByEmailId(registeredWallet.getEmail(), 1000.0);

        Wallet foundWallet = this.walletService.getWalletByEmailId(registeredWallet.getEmail());

        assertEquals(4000.0, foundWallet.getBalance());
    }
// Negative test case for withdrawing funds with insufficient balance
    @Test
    void withdrawInsufficientFundsShouldThrowExceptionTest() throws WalletException {
        this.walletService.registerNewUserWallet(wallet);

        WalletWithdrawFundException exception = assertThrows(WalletWithdrawFundException.class,
                () -> this.walletService.withdrawFundsFromWalletByEmailId(wallet.getEmail(), 6000.0));

        assertEquals("Insufficient funds in the wallet to withdraw", exception.getMessage());
    }

    @Test
    void withdrawFromNonExistentWalletShouldThrowExceptionTest() {
        assertThrows(WalletNotFoundException.class,
                () -> this.walletService.withdrawFundsFromWalletByEmailId("nonexistent@gmail.com", 100.0));
    }

    @Test
    void withdrawNegativeAmountShouldThrowExceptionTest() throws WalletException {
        // Test for withdrawing a negative amount from a wallet.
        this.walletService.registerNewUserWallet(wallet);

        WalletWithdrawFundException exception = assertThrows(WalletWithdrawFundException.class,
                () -> this.walletService.withdrawFundsFromWalletByEmailId(wallet.getEmail(), -50.0));

        assertEquals("Amount is less than 0, cannot add funds", exception.getMessage());
    }

    @Test
    void transferFundsTest() throws WalletException, WalletTransferFundException, WalletNotFoundException {
        Wallet senderWallet = new Wallet(null, "Sender", "sender@gmail.com", "Pass@123", 1000.0);
        Wallet receiverWallet = new Wallet(null, "Receiver", "receiver@gmail.com", "Pass@465", 200.0);

        this.walletService.registerNewUserWallet(senderWallet);
        this.walletService.registerNewUserWallet(receiverWallet);

        Boolean success = this.walletService.transferFunds(senderWallet.getEmail(), receiverWallet.getEmail(), 300.0);
        assertTrue(success);

        Wallet updatedSender = this.walletService.getWalletByEmailId(senderWallet.getEmail());
        Wallet updatedReceiver = this.walletService.getWalletByEmailId(receiverWallet.getEmail());

        assertEquals(700.0, updatedSender.getBalance());
        assertEquals(500.0, updatedReceiver.getBalance());

        this.walletService.deleteWalletByEmailId(senderWallet.getEmail());
        this.walletService.deleteWalletByEmailId(receiverWallet.getEmail());
    }

    @Test
    void transferFundsInsufficientBalanceTest() throws WalletException, WalletNotFoundException {
        // Test for transferring funds with insufficient balance in the sender's wallet.
        Wallet senderWallet = new Wallet(null, "Sender", "sender@gmail.com", "P12!ass1", 100.0);
        Wallet receiverWallet = new Wallet(null, "Receiver", "receiver@gmail.com", "P!23ass2", 200.0);

        this.walletService.registerNewUserWallet(senderWallet);
        this.walletService.registerNewUserWallet(receiverWallet);

        WalletTransferFundException exception = assertThrows(WalletTransferFundException.class,
                () -> this.walletService.transferFunds(senderWallet.getEmail(), receiverWallet.getEmail(), 500.0));

        assertEquals("Insufficient funds in the sender wallet to transfer", exception.getMessage());

        this.walletService.deleteWalletByEmailId(senderWallet.getEmail());
        this.walletService.deleteWalletByEmailId(receiverWallet.getEmail());
    }

    @Test
    void transferFundsNonExistentSenderTest() throws WalletException, WalletNotFoundException {
        Wallet receiverWallet = new Wallet(null, "Receiver", "receiver@gmail.com", "Pass@123", 200.0);
        this.walletService.registerNewUserWallet(receiverWallet);

        assertThrows(WalletNotFoundException.class,
                () -> this.walletService.transferFunds("nonexistent@gmail.com", receiverWallet.getEmail(), 100.0));

        this.walletService.deleteWalletByEmailId(receiverWallet.getEmail());
    }

    @Test
    void transferFundsNonExistentReceiverTest() throws WalletException, WalletNotFoundException {
        // Test for transferring funds to a non-existent receiver wallet.
        Wallet senderWallet = new Wallet(null, "Sender", "sender@gmail.com", "Pass!123", 1000.0);
        this.walletService.registerNewUserWallet(senderWallet);

        assertThrows(WalletNotFoundException.class,
                () -> this.walletService.transferFunds(senderWallet.getEmail(), "nonexistent@gmail.com", 100.0));

        this.walletService.deleteWalletByEmailId(senderWallet.getEmail());
    }

    @Test
    void transferNegativeAmountShouldThrowExceptionTest() throws WalletException, WalletNotFoundException {
        // Test for transferring a negative amount of funds.
        Wallet senderWallet = new Wallet(null, "Sender", "sender@gmail.com", "passP1!@#@", 1000.0);
        Wallet receiverWallet = new Wallet(null, "Receiver", "receiver@gmail.com", "pass123@P2", 200.0);

        this.walletService.registerNewUserWallet(senderWallet);
        this.walletService.registerNewUserWallet(receiverWallet);

        WalletTransferFundException exception = assertThrows(WalletTransferFundException.class,
                () -> this.walletService.transferFunds(senderWallet.getEmail(), receiverWallet.getEmail(), -50.0));

        assertEquals("Amount is less than 0, cannot transfer funds", exception.getMessage());

        this.walletService.deleteWalletByEmailId(senderWallet.getEmail());
        this.walletService.deleteWalletByEmailId(receiverWallet.getEmail());
    }

    @Test
    void getAllCustomerWalletsTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving all customer wallets.
        Wallet wallet1 = new Wallet(null, "User one", "user1@gmail.com", "passP1!@#@", 100.0);
        Wallet wallet2 = new Wallet(null, "Usee two", "user2@gmail.com", "passP1!@#@", 200.0);
        this.walletService.registerNewUserWallet(wallet1);
        this.walletService.registerNewUserWallet(wallet2);

        Collection<Wallet> wallets = this.walletService.getAllCustomerWallets();
        assertNotNull(wallets);
        assertFalse(wallets.isEmpty());
        assertEquals(2, wallets.size()); // Assuming only these two wallets are in DB now

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
        this.walletService.deleteWalletByEmailId(wallet2.getEmail());
    }

    @Test
    void getAllCustomerWalletsWhenNoneExistTest() {
        // Test for retrieving all customer wallets when no wallets are registered.
        assertThrows(WalletNotFoundException.class, () -> this.walletService.getAllCustomerWallets());
    }

    @Test
    void getWalletByEmailIdTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving a wallet by its email ID.
        Wallet registeredWallet = this.walletService.registerNewUserWallet(wallet);

        Wallet foundWallet = this.walletService.getWalletByEmailId(registeredWallet.getEmail());
        assertNotNull(foundWallet);
        assertEquals(registeredWallet.getEmail(), foundWallet.getEmail());
    }

    @Test
    void getNonExistentWalletByEmailIdShouldThrowExceptionTest() {
        // Test for retrieving a wallet using a non-existent email ID.
        assertThrows(WalletNotFoundException.class, () -> this.walletService.getWalletByEmailId("nonexistent@gmail.com"));
    }

    @Test
    void getWalletByIdTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving a wallet by its ID.
        Wallet registeredWallet = this.walletService.registerNewUserWallet(wallet);

        Wallet foundWallet = this.walletService.getWalletById(registeredWallet.getWalletId());
        assertNotNull(foundWallet);
        assertEquals(registeredWallet.getWalletId(), foundWallet.getWalletId());
    }

    @Test
    void getNonExistentWalletByIdShouldThrowExceptionTest() {
        // Test for retrieving a wallet using a non-existent ID.
        assertThrows(WalletNotFoundException.class, () -> this.walletService.getWalletById(99999));
    }

    @Test
    void updateWalletByEmailIdTest() throws WalletException, WalletNotFoundException {
        // Test for successfully updating wallet details by email ID.
        Wallet registeredWallet = this.walletService.registerNewUserWallet(wallet);

        Wallet updatedDetails = new Wallet(null, "NewName", "name@gmail.com", "New@Pass123", 5000.0);
        Wallet resultWallet = this.walletService.updateWalletByEmailId(registeredWallet.getEmail(), updatedDetails);

        assertNotNull(resultWallet);
        assertEquals("NewName", resultWallet.getCustomerName());
        assertEquals("New@Pass123", resultWallet.getPassword());
        assertEquals(registeredWallet.getEmail(), resultWallet.getEmail());
    }

    @Test
    void updateNonExistentWalletShouldThrowExceptionTest() {
        // Test for updating a wallet that does not exist.
        Wallet updatedDetails = new Wallet(null, "NewName", "nonexistent@gmail.com", "NewPass123", 6000.0);
        assertThrows(WalletNotFoundException.class,
                () -> this.walletService.updateWalletByEmailId("nonexistent@gmail.com", updatedDetails));
    }

    @Test
    void getWalletsByBalanceRangeTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving wallets within a specified balance range.
        Wallet wallet1 = new Wallet(null, "User one", "user1@gmail.com", "passP1!@#@", 100.0);
        Wallet wallet2 = new Wallet(null, "User two", "user2@gmail.com", "passP1!@#@", 500.0);
        Wallet wallet3 = new Wallet(null, "User three", "user3@gmail.com", "passP1!@#@", 1500.0);
        this.walletService.registerNewUserWallet(wallet1);
        this.walletService.registerNewUserWallet(wallet2);
        this.walletService.registerNewUserWallet(wallet3);

        Collection<Wallet> wallets = this.walletService.getWalletsByBalanceRange(200.0, 1000.0);
        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        assertEquals("user2@gmail.com", wallets.iterator().next().getEmail());

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
        this.walletService.deleteWalletByEmailId(wallet2.getEmail());
        this.walletService.deleteWalletByEmailId(wallet3.getEmail());
    }

    @Test
    void getWalletsByBalanceRangeNoMatchTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving wallets within a range with no matching wallets.
        Wallet wallet1 = new Wallet(null, "User one", "user1@gmail.com", "passP1!@#@", 100.0);
        this.walletService.registerNewUserWallet(wallet1);

        assertThrows(WalletException.class, () -> this.walletService.getWalletsByBalanceRange(200.0, 300.0));

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
    }

    @Test
    void getWalletsByExactBalanceTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving wallets with an exact balance.
        Wallet wallet1 = new Wallet(null, "Userone", "user1@gmail.com", "passP1!@#@", 100.0);
        Wallet wallet2 = new Wallet(null, "User two", "user2@gmail.com", "passP1!@#@", 500.0);
        this.walletService.registerNewUserWallet(wallet1);
        this.walletService.registerNewUserWallet(wallet2);

        Collection<Wallet> wallets = this.walletService.getWalletsByExactBalance(100.0);
        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        assertEquals("user1@gmail.com", wallets.iterator().next().getEmail());

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
        this.walletService.deleteWalletByEmailId(wallet2.getEmail());
    }

    @Test
    void getWalletsByExactBalanceNoMatchTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving wallets with an exact balance that does not exist.
        Wallet wallet1 = new Wallet(null, "User one", "user1@gmail.com", "passP1!@#@", 100.0);
        this.walletService.registerNewUserWallet(wallet1);

        assertThrows(WalletException.class, () -> this.walletService.getWalletsByExactBalance(200.0));

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
    }

    @Test
    void getWalletsByEmailDomainTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving wallets by email domain.
        Wallet wallet1 = new Wallet(null, "Usera", "user1@ford.com", "passP1!@#@", 100.0);
        Wallet wallet2 = new Wallet(null, "Userb", "user2@gmail.com", "passP1assP1!@#@", 500.0);
        Wallet wallet3 = new Wallet(null, "Userc", "user3@ford.com", "passsP1!@#@3", 1500.0);
        this.walletService.registerNewUserWallet(wallet1);
        this.walletService.registerNewUserWallet(wallet2);
        this.walletService.registerNewUserWallet(wallet3);

        Collection<Wallet> wallets = this.walletService.getWalletsByEmailDomain("ford.com");
        assertNotNull(wallets);
        assertEquals(2, wallets.size());
        Optional<Wallet> foundWallet1 = wallets.stream().filter(w -> w.getEmail().equals("user1@ford.com")).findFirst();
        assertTrue(foundWallet1.isPresent());
        Optional<Wallet> foundWallet2 = wallets.stream().filter(w -> w.getEmail().equals("user3@ford.com")).findFirst();
        assertTrue(foundWallet2.isPresent());

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
        this.walletService.deleteWalletByEmailId(wallet2.getEmail());
        this.walletService.deleteWalletByEmailId(wallet3.getEmail());
    }

    @Test
    void getWalletsByEmailDomainNoMatchTest() throws WalletException, WalletNotFoundException {
        // Test for retrieving wallets by email domain with no matching wallets.
        Wallet wallet1 = new Wallet(null, "Usera", "user1@ford.com", "assP1!@#@as", 100.0);
        this.walletService.registerNewUserWallet(wallet1);

        assertThrows(WalletException.class, () -> this.walletService.getWalletsByEmailDomain("example.com"));

        this.walletService.deleteWalletByEmailId(wallet1.getEmail());
    }


    @AfterEach
    void cleanUpAfterEachTestCase() {
        this.walletRepository.deleteAll();
    }
}

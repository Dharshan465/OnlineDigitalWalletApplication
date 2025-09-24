package ford.assignment_16_20.OnlineDigitalWalletApplication;

import ford.assignment_16_20.OnlineDigitalWalletApplication.Repository.WalletRepository;
import ford.assignment_16_20.OnlineDigitalWalletApplication.model.Wallet;
import jakarta.persistence.EntityManager; // Import EntityManager for clearing persistence context
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class WalletRepositoryTests {

    @Autowired
    private WalletRepository walletRepository;

    private List<Wallet> walletList = new ArrayList<>();

    public WalletRepositoryTests() {

        walletList.add(new Wallet("John Doe", "john@gmail.com","secur@ePass1", 1000.0));
        walletList.add(new Wallet("Jane Smith", "jane@gmail.com","Secur@ePass2", 2500.0));
        walletList.add(new Wallet("Alice Johnson", "alice@gmail.com","Secur@ePass3", 500.0));
        walletList.add(new Wallet("Bob Brown", "bob@gmail.com","Secur@ePass4", 7500.0));

    }

    @BeforeEach
    void setUpBeroreEachTest(){
        walletRepository.deleteAllInBatch();


        this.walletRepository.saveAll(walletList);

    }

    @Test
    void getWalletsWithBalanceRange(){
        // Ensure that the names are what you expect after setup
        // The IDs will be auto-generated, likely starting from 1 in H2 for each test run.
        Collection<Wallet> walletList1= this.walletRepository.findWalletsByBalanceBetween(1000.0,3500.0);

        // Based on your data: John Doe (1000.0), Jane Smith (2500.0), Peter Jones (3000.0)
        // These three wallets fall within the 1000.0 to 3500.0 range (inclusive).
        Assertions.assertEquals(2,walletList1.size());

        // You could also add assertions to check specific wallet properties if needed
        // For example:
        // assertThat(walletList1).extracting(Wallet::getCustomerName)
        //                        .containsExactlyInAnyOrder("John Doe", "Jane Smith", "Peter Jones");
    }

    @AfterEach
    void cleanUpAfterEachTest(){
        this.walletRepository.deleteAllInBatch(); // Use deleteAllInBatch for efficiency
    }
}

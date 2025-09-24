package ford.assignment_16_20.OnlineDigitalWalletApplication;

import ford.assignment_16_20.OnlineDigitalWalletApplication.Repository.WalletRepository;
import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.WalletException;
import ford.assignment_16_20.OnlineDigitalWalletApplication.model.Wallet;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OnlineDigitalWalletApplicationTests {

    @LocalServerPort
    int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private WalletRepository walletRepository;

    @Test
    void createWalletAndTestBalance() throws WalletException {
        try{
        Wallet walletReq = new Wallet(1, "Ford",  "email@ford.com", "123P@ford", 45000.0);

        ResponseEntity<Wallet> walletResponseEntity = testRestTemplate.<Wallet>postForEntity("http://localhost:" + port + "/api/v1/wallet", walletReq, Wallet.class);
        Assertions.assertEquals(HttpStatus.CREATED, walletResponseEntity.getStatusCode());
        Wallet wallet = walletResponseEntity.getBody();
        AssertionErrors.assertNotNull("Wallet object cant be null", wallet);
        Assertions.assertEquals(45000.0, wallet.getBalance());
    }catch(Exception e){
           throw new WalletException(e.getMessage());
        }
    }

    @AfterEach
    void cleanUp() {
        this.walletRepository.deleteAll();
    }


}
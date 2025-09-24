package ford.assignment_16_20.OnlineDigitalWalletApplication;

import ford.assignment_16_20.OnlineDigitalWalletApplication.Repository.WalletRepository;
import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.WalletException;
import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.WalletNotFoundException;
import ford.assignment_16_20.OnlineDigitalWalletApplication.model.Wallet;
import ford.assignment_16_20.OnlineDigitalWalletApplication.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;

import java.nio.channels.ScatteringByteChannel;
import java.util.Optional;

@SpringBootTest
public class WalletServiceMockTests {

    //    @MockBean
    @MockitoBean
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;
    @Test
    void testGetsUser(){
        // stubbing of proxy object
        Mockito.when(walletRepository.findById(1))
                .thenReturn(Optional.of(new Wallet(1,"Name","name@ford.com","Pass@123",1000.0)));
        Mockito.when(walletRepository.findById(2))
                .thenReturn(Optional.of(new Wallet(2,"Name two","name2@ford.com","Pass@123",2000.0)));

        try {

            Wallet wallet = walletService.getWalletById(2);
            Assertions.assertEquals("Name two",wallet.getCustomerName());

        } catch (WalletNotFoundException | WalletException e) {
            throw new RuntimeException(e);
        }

    }


}


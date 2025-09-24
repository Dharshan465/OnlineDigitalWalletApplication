package ford.assignment_16_20.OnlineDigitalWalletApplication;

import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.WalletException;
import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.WalletNotFoundException;
import ford.assignment_16_20.OnlineDigitalWalletApplication.model.Wallet;
import ford.assignment_16_20.OnlineDigitalWalletApplication.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Add test methods here to test the WalletController endpoints

    @MockitoBean
    private WalletService walletService;

    @Test
    void testGetWalletById() throws Exception {
        try{
        Mockito.when(walletService.getWalletById(1))
                .thenReturn(new Wallet(1, "Name", "name@ford.com", "Name@1234", 1000.0));

        mockMvc.perform(get("/api/v1/wallet/id/1"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.customerName").value("Name")));

    }catch (WalletNotFoundException e){
            e.printStackTrace();
        }
    }


}

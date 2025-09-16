package ford.assignment16.OnlineDigitalWalletApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet not found for the Wallet ID.")
public class WalletNotFoundException extends Exception {
    public WalletNotFoundException(String message) {
        super(message);
    }
}

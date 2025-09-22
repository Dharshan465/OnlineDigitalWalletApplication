package ford.assignment_16_20.OnlineDigitalWalletApplication.exception;

//@ResponseStatus(HttpStatus.BAD_REQUEST) // Or HttpStatus.INTERNAL_SERVER_ERROR
public class WalletException extends Exception {
    public WalletException(String message) {
        super(message);
    }
}

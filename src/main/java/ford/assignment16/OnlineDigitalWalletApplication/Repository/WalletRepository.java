package ford.assignment16.OnlineDigitalWalletApplication.Repository;

import ford.assignment16.OnlineDigitalWalletApplication.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByEmail(String email);

    Collection<Wallet> findWalletsByBalanceBetween(Double fromBalance, Double toBalance);

    Collection<Wallet> findWalletByBalance(double balance);

    Collection<Wallet>findWalletByEmailContainingIgnoreCase(String email);
}

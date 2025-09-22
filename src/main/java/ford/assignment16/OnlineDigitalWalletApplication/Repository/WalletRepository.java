package ford.assignment16.OnlineDigitalWalletApplication.Repository;

import ford.assignment16.OnlineDigitalWalletApplication.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByEmail(String email);

    Collection<Wallet> findWalletsByBalanceBetween(Double fromBalance, Double toBalance);

    Collection<Wallet> findWalletByBalance(double balance);

    Collection<Wallet>findWalletByEmailContainingIgnoreCase(String email);
}

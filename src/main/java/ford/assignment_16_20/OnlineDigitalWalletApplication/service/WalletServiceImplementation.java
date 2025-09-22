package ford.assignment_16_20.OnlineDigitalWalletApplication.service;

import ford.assignment_16_20.OnlineDigitalWalletApplication.Repository.WalletRepository;
import ford.assignment16.OnlineDigitalWalletApplication.exception.*;
import ford.assignment_16_20.OnlineDigitalWalletApplication.exception.*;
import ford.assignment_16_20.OnlineDigitalWalletApplication.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class WalletServiceImplementation implements WalletService{

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImplementation(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet registerNewUserWallet(Wallet newWallet) throws WalletException {

        Wallet foundWallet = walletRepository.findByEmail(newWallet.getEmail());
        if (foundWallet != null) {
            throw new WalletException("User already exists with emailId: " + newWallet.getEmail());
        } else {
            walletRepository.save(newWallet);
        }
        return newWallet;
    }

    @Override
    public Wallet deleteWalletByEmailId(String emailId) throws WalletNotFoundException, WalletException {

        Wallet foundWallet = walletRepository.findByEmail(emailId);
        if (foundWallet == null) {
            throw new WalletNotFoundException("User not found with emailId: " + emailId);
        } else {
            walletRepository.delete(foundWallet);
            return foundWallet;
        }
    }

    @Override
    public Double addFundsToWalletByEmailId(String emailId, Double amount) throws WalletAddFundException, WalletNotFoundException, WalletException {

            Wallet foundWallet = walletRepository.findByEmail(emailId);
            if (foundWallet == null) {
                throw new WalletNotFoundException("User not found with emailId: " + emailId);
            } else {

                if (amount <= 0) {
                    throw new WalletAddFundException("Amount is less than 0, cannot add funds");
                } else {
                    double newBalance = foundWallet.getBalance() + amount;
                    foundWallet.setBalance(newBalance);
                    walletRepository.save(foundWallet);
                    return newBalance;
                }
            }

    }


    @Override
    public Double withdrawFundsFromWalletByEmailId(String emailId, Double amount) throws WalletWithdrawFundException, WalletNotFoundException, WalletException {

            Wallet foundWallet = walletRepository.findByEmail(emailId);
            if (foundWallet == null) {
                throw new WalletNotFoundException("User not found with emailId: " + emailId);
            } else {
                if (amount <= 0) {
                    throw new WalletWithdrawFundException("Amount is less than 0, cannot add funds");
                } else if (foundWallet.getBalance() < amount) {
                    throw new WalletWithdrawFundException("Insufficient funds in the wallet to withdraw");
                } else {
                    double newBalance = foundWallet.getBalance() - amount;
                    foundWallet.setBalance(newBalance);
                    walletRepository.save(foundWallet);
                    return newBalance;
                }
            }
        }


    @Override
    public Collection<Wallet> getAllCustomerWallets() throws WalletNotFoundException {

            Collection<Wallet> allWallets=walletRepository.findAll();
            if(!allWallets.isEmpty()){
                return allWallets;
            }else{
                throw new WalletNotFoundException("No wallets found in the database");
            }
        }


    @Override
    public Wallet getWalletByEmailId(String emailId) throws WalletNotFoundException, WalletException {

            Wallet foundWallet=walletRepository.findByEmail(emailId);
            if(foundWallet==null){
                throw new WalletNotFoundException("User not found with emailId: "+emailId);
            }else{
                return foundWallet;
            }
    }



    @Override
    public Wallet getWalletById(Integer walletId) throws WalletNotFoundException, WalletException {

            Optional<Wallet> foundWallet=walletRepository.findById(walletId);
            if(foundWallet.isEmpty()){
                throw new WalletNotFoundException("User not found with walletId: "+walletId);
            }else{
                return foundWallet.orElse(null);
            }

    }

    @Override
    public Wallet updateWalletByEmailId(String emailId, Wallet newWallet) throws WalletNotFoundException, WalletException {

            Wallet foundWallet=walletRepository.findByEmail(emailId);
            if(foundWallet==null){
                throw new WalletNotFoundException("User not found with emailId: "+emailId);
            }else{
                //update the wallet details
                foundWallet.setCustomerName(newWallet.getCustomerName());
                foundWallet.setPassword(newWallet.getPassword());
                walletRepository.save(foundWallet);
                return foundWallet;
            }
    }

    @Override
    public Collection<Wallet> getWalletsByBalanceRange(Double min, Double max) throws WalletException {
        Collection<Wallet> allWallets = walletRepository.findWalletsByBalanceBetween(min, max);
        if (!allWallets.isEmpty()) {
            return allWallets;
        }else{
            throw new WalletException("No wallets found in the database");
        }
    }

    @Override
    public Collection<Wallet> getWalletsByExactBalance(Double balance) throws WalletException {
        Collection<Wallet> allWallets = walletRepository.findWalletByBalance(balance);
        if (!allWallets.isEmpty()) {
            return allWallets;
        }else{
            throw new WalletException("No wallets found in the database");
        }
    }

    @Override
    public Collection<Wallet> getWalletsByEmailDomain(String domain) throws WalletException {
        Collection<Wallet> allWallets = walletRepository.findWalletByEmailContainingIgnoreCase(domain);
        if (!allWallets.isEmpty()) {
            return allWallets;
        }else{
            throw new WalletException("No wallets found in the database");
        }
    }

    @Override
    public Boolean transferFunds(String fromEmailId, String toEmailId, Double amount) throws WalletTransferFundException, WalletNotFoundException, WalletException {

            Wallet fromWallet=walletRepository.findByEmail(fromEmailId);
            Wallet toWallet=walletRepository.findByEmail(toEmailId);
            if(fromWallet==null){
                throw new WalletNotFoundException("Sender wallet not found with emailId: "+fromEmailId);
            }else if(toWallet==null){
                throw new WalletNotFoundException("Receiver wallet not found with emailId: "+toEmailId);
            }else{
                if(amount<=0){
                    throw new WalletTransferFundException("Amount is less than 0, cannot transfer funds");
                }else if(fromWallet.getBalance()<amount){
                    throw new WalletTransferFundException("Insufficient funds in the sender wallet to transfer");
                }else{
                    //withdraw from sender wallet
                    double newFromBalance=fromWallet.getBalance()-amount;
                    fromWallet.setBalance(newFromBalance);
                    walletRepository.save(fromWallet);

                    //add to receiver wallet
                    double newToBalance=toWallet.getBalance()+amount;
                    toWallet.setBalance(newToBalance);
                    walletRepository.save(toWallet);
                    return true;
                }
            }
        }
    }


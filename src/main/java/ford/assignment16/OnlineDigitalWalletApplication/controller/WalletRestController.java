package ford.assignment16.OnlineDigitalWalletApplication.controller;

import ford.assignment16.OnlineDigitalWalletApplication.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import ford.assignment16.OnlineDigitalWalletApplication.exception.*;
import ford.assignment16.OnlineDigitalWalletApplication.model.Wallet;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletRestController {

    private final WalletService walletService;


    @Autowired
    public WalletRestController(WalletService walletService) {
        this.walletService = walletService;
    }


    @PostMapping
    public String createWallet(@Valid @RequestBody Wallet wallet) throws WalletException {
        this.walletService.registerNewUserWallet(wallet);
        return "User wallet created successfully with emailId: " + wallet.getEmail();

    }

    @GetMapping("/emailId")
    public Wallet getWalletByEmail(@Valid @RequestParam(value = "emailId", defaultValue = "email", required = false) String email) throws WalletException, WalletNotFoundException {
        return this.walletService.getWalletByEmailId(email);
    }


    @GetMapping("/id/{walletId}")
    public Wallet getUserWalletById(@PathVariable Integer walletId) throws WalletException, WalletNotFoundException {
        return this.walletService.getWalletById(walletId);
    }


    @GetMapping("/allWallets")
    public Collection<Wallet> getAllWallets() throws WalletException, WalletNotFoundException {
        return this.walletService.getAllCustomerWallets();
    }


    @PutMapping("/update")
    public Wallet updateWallet(@Valid @RequestBody Wallet wallet) throws WalletException, WalletNotFoundException {
        return this.walletService.updateWalletByEmailId(wallet.getEmail(), wallet);
    }


    //DTO to get parameters
    @PatchMapping("/addfunds")
    public Double addFundsToWallet(@RequestParam(value = "emailId", defaultValue = "email", required = false) String emailId, @RequestParam(value = "amount", defaultValue = "0.0", required = false) Double amount) throws WalletException, WalletNotFoundException, WalletAddFundException {
        return this.walletService.addFundsToWalletByEmailId(emailId, amount);
    }


    @PatchMapping("/withdrawfunds")
    public Double withdrawFundsFromWallet(@RequestParam(value = "emailId", defaultValue = "email", required = false) String emailId, @RequestParam(value = "amount", defaultValue = "0.0", required = false) Double amount) throws WalletException, WalletNotFoundException, WalletWithdrawFundException {
        return this.walletService.withdrawFundsFromWalletByEmailId(emailId, amount);
    }

    @PatchMapping("/transferfunds")
    public String transferFundsBetweenWallets(@RequestParam(value = "fromEmailId", defaultValue = "fromEmail", required = false) String fromEmailId, @RequestParam(value = "toEmailId", defaultValue = "toEmail", required = false) String toEmailId, @RequestParam(value = "amount", defaultValue = "0.0", required = false) Double amount) throws WalletException, WalletNotFoundException, WalletTransferFundException {
        this.walletService.transferFunds(fromEmailId, toEmailId, amount);
        return "Fund Transfer Successful from " + fromEmailId + " to " + toEmailId + " of amount: " + amount;
    }

    @DeleteMapping("/{emailId}")
    public String deleteUserWalletByEmailId(@PathVariable String emailId) throws WalletException, WalletNotFoundException {
        this.walletService.deleteWalletByEmailId(emailId);
        return "User wallet deleted successfully with emailId: " + emailId;

    }

    @GetMapping("/balanceRange")
    public Collection<Wallet> getWalletsBalanceRange(
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) throws WalletException, WalletNotFoundException {
        return this.walletService.getWalletsByBalanceRange(min, max);
    }

    @GetMapping("/zeroBalance")
    public Collection<Wallet> getWalletsWithZeroBalance(@RequestParam (defaultValue = "0.00", required = false) Double balance) throws WalletException {
        return this.walletService.getWalletsByExactBalance(balance);
    }

    @GetMapping("/emailDomain")
    public Collection<Wallet> getWalletsByEmailDomain(@RequestParam (defaultValue = "domain", required = false) String domain) throws WalletException, WalletNotFoundException {
        return this.walletService.getWalletsByEmailDomain(domain);
    }

    }






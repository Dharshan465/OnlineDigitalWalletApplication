package ford.assignment16.OnlineDigitalWalletApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Wallet {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int walletId;

    @NotNull(message = "Customer name cannot be null")
    @NotBlank(message = "Customer name cannot be blank")
    @Size(min=3, max=50 , message="Customer name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Customer name must contain only alphabets and spaces")
    private String customerName;

    @NotNull(message = "Balance cannot be null")
    //@NotBlank(message = "Balance cannot be blank")
    private double balance;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9.+%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be bla nk")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    public Wallet() {
    }

    public Wallet(int walletId, String customerName, String email, String password, double balance) {
        this.walletId = walletId;
        this.customerName = customerName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Wallet { Id :"+ walletId +
                ", Customer Name: '" + customerName + '\'' +
                ", Balance: " + balance +
                ", Email: '" + email + '\'' +
                ", Password: '" + password + '\'' +
                "}\n";
    }
}

package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DialectOverride;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(name = "balance_cents", nullable = false)
    private double balance;

    @Version
    private int version;

    public Account() {}

    public Account(double balance) {
        this.balance = balance;
    }

    public Long getNumber() { return number; }

    public double getBalance() { return balance; }

    public double setBalance(double balance) { return this.balance = balance; }

    public void setBalance(int balance) { this.balance = balance; }

    public int getVersion() { return version; }
}

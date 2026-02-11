package com.spring.tradex.Models;

import com.spring.tradex.Enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal walletBalance;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Portfolio> portfolioItems;

    public User(String firstname, String lastname, String email, String password,Role role, BigDecimal walletBalance,
                List<Portfolio> portfolioItems) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.walletBalance = BigDecimal.ZERO;
    }

    public void debitWallet(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Debit amount must be positive");
        }

        if(this.walletBalance.compareTo(amount) < 0){
            throw new IllegalStateException("Insufficient Balance");
        }
        this.walletBalance = this.walletBalance.subtract(amount);
    }

    public void creditWallet(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Credit amount must be positive");
        }
    }

    public static User register(
            String firstname,
            String lastname,
            String email,
            String encodedPassword,
            Role role
    ){
        User user = new User();
        user.firstname = firstname;
        user.lastname = lastname;
        user.email = email;
        user.password = encodedPassword;
        user.role = role;
        return user;

    }




}

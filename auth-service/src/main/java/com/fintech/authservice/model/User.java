package com.fintech.authservice.model;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    // Kullanıcı bilgileri
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // Kullanıcı hesap durumu
    @CreationTimestamp
    @Column(name = "created_at" ,nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt; // softdelete mantıgı ile silme

    // Token geçersiz kılma süreci için
    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    public User() {
        // JPA için boş constructor
    }

    public User(String email, String fullName, String password, String phoneNumber, Role role) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email; // login alanı email
    }

    @Override // Hesap etkin mi? (true ise etkin)
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }


    // Hesap süresi dolmus mu? (true ise dolmamıs)
    // TODO : Eğer bir süre belirlenirse burası guncellenecek
    @Override 
    public boolean isAccountNonExpired() {
        return true;
    }

    // Hesap kilitli mi? (true ise kilitli degil)
    // TODO : Eğer bir kilitleme mekanizması eklenirse burası guncellenecek (3 basarısız giris gibi)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Sifre suresi dolmus mu? (true ise dolmamıs)
    // TODO : Eğer bir sifre suresi belirlenirse burası guncellenecek
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    

    
    


    
}

package com.fintech.authservice.model;

import javax.persistence.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.UUID;
import com.fintech.common.event.Role;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // Kullanıcı hesap durumu
    @CreationTimestamp
    @Column(name = "created_at" ,nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "deleted_at")
    private Instant deletedAt = null; // softdelete mantıgı ile silme

    // Token geçersiz kılma süreci için
    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    public User() {
        // JPA için boş constructor
    }

    public User(String email, String passwordHash, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
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

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }


    public Integer getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Integer tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    @Override
    public String getPassword() {
       return this.passwordHash;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    

    
}

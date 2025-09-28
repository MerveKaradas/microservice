package com.fintech.userservice.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name= "users")
public class User {

    @Id
    private Long userId;      // Auth Service’den gelen UUID
    private String firstName;
    private String lastName;
    private String email;       // Auth Service’den gelir, burada read-only
    private String phoneNumber;
    private String nationalId;  // TC No (PII → encrypt/mask gerekebilir)
    private Role role;        // CUSTOMER / ADMIN
    private String status;      // ACTIVE / BLOCKED / CLOSED
    private Instant createdAt;
    private Instant updatedAt;


    User() {

    }

    

    public User(Long userId, String firstName, String lastName, String email, String phoneNumber, String nationalId,
            Role role, String status, Instant createdAt, Instant updatedAt) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nationalId = nationalId;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getNationalId() {
        return nationalId;
    }
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    

    
    
}

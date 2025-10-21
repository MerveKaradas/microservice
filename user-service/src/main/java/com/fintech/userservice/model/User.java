package com.fintech.userservice.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fintech.common.event.auth.Role;
import com.fintech.common.event.user.ProfileStatus;

import javax.persistence.Id;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name= "users")
public class User {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id; // Auth Service’den gelen UUID
    private String firstName;
    private String lastName;
    private String email;  // Auth Service’den gelir, burada read-only
    private String phoneNumber;

    @Convert(converter = com.fintech.userservice.security.TcNoAttributeConverter.class)
    private String nationalId; 

    @Enumerated(EnumType.STRING)
    private Role role;        
   
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus = ProfileStatus.INCOMPLETE;
 
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt; // softdelete mantıgı ile silme
    private Integer tokenVersion; // Auth Service'den gelir, burada read-only


    // Default constructor (JPA için)
    protected User() {}

    // Private constructor Builder kullanacak
    private User(Builder builder) {
        this.id = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.nationalId = builder.nationalId;
        this.role = builder.role;
        this.profileStatus = builder.profileStatus;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.deletedAt = builder.deletedAt;
        this.tokenVersion = builder.tokenVersion;
    }

    // Soft delete & restore
    public void softDelete() { this.deletedAt = Instant.now(); }
    public void restore() { this.deletedAt = null; }

    // Getters
    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }


    public String getNationalId() {
        if(nationalId != null && nationalId.length()==11) {
            return nationalId.substring(0, 3) + "*****" + nationalId.substring(8);
        }
        return null;
    }
    public Role getRole() { return role; }
    public ProfileStatus getProfileStatus() { return profileStatus; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public Integer getTokenVersion() { return tokenVersion;} 

    public void setId(UUID userId) {this.id = userId;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setNationalId(String nationalId) {this.nationalId = nationalId;}
    public void setRole(Role role) {this.role = role;}
    public void setProfileStatus(ProfileStatus profileStatus) {this.profileStatus = profileStatus;}
    public void setCreatedAt(Instant createdAt) {this.createdAt = createdAt;}
    public void setUpdatedAt(Instant updatedAt) {this.updatedAt = updatedAt;}
    public void setDeletedAt(Instant deletedAt) {this.deletedAt = deletedAt;}
    public void setTokenVersion(Integer tokenVersion) {this.tokenVersion = tokenVersion;}
   
    

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID userId;      
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String nationalId;  
        private Role role;        
        private ProfileStatus profileStatus = ProfileStatus.INCOMPLETE;
        private Instant createdAt;
        private Instant updatedAt;
        private Instant deletedAt;
        private Integer tokenVersion;

        public Builder userId(UUID userId) { this.userId = userId; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder nationalId(String nationalId) { this.nationalId = nationalId; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder profileStatus(ProfileStatus profileStatus) { this.profileStatus = profileStatus; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder deletedAt(Instant deletedAt) { this.deletedAt = deletedAt; return this; }
        public Builder tokenVersion(Integer tokenVersion) { this.tokenVersion = tokenVersion; return this;}

        public User build() {
            return new User(this);
        }
    }

   
     

    
    
}

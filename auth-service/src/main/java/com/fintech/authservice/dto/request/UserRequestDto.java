package com.fintech.authservice.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto implements Serializable{

    private static final long serialVersionUID = 1L;

    @Email(message = "Geçerli bir email adresi giriniz")
    @NotBlank(message = "Email boş olamaz")
    private String email;

    @NotBlank(message = "Ad soyad boş olamaz")
    private String fullName;

    @NotBlank(message = "Parola boş olamaz!")
    @Size(min = 8, max = 100, message = "Parola 8-100 karakter arasında olmalıdır!")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Parola en az bir büyük harf, bir küçük harf, bir sayı ve bir özel karakter içermeli"
    )
    private String password;

    @NotBlank(message = "Telefon boş olamaz")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", 
             message = "Telefon numarası geçerli olmalı")
    private String phoneNumber;


    public UserRequestDto(
            String email,
            String fullName,
            String password,
            String phoneNumber,
            String role) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        
    }


    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    

    
    
    
}

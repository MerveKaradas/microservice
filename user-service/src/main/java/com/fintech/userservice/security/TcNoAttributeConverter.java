package com.fintech.userservice.security;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.stereotype.Component;

@Component 
@Converter
public class TcNoAttributeConverter implements AttributeConverter<String, String>  {

    private final TcNoEncryptor tcEncryptor;

    public TcNoAttributeConverter(TcNoEncryptor tcEncryptor) {
        this.tcEncryptor = tcEncryptor;
    }

    @Override
    public String convertToDatabaseColumn(String tcknPlain) {
        if (tcknPlain == null) return null;
        return tcEncryptor.encrypt(tcknPlain);
    }

    @Override
    public String convertToEntityAttribute(String tcknEncrypted) {
        if (tcknEncrypted == null) return null;
        return tcEncryptor.decrypt(tcknEncrypted);
    }
}

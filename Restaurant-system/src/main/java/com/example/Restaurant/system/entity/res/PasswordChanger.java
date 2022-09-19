package com.example.Restaurant.system.entity.res;

import lombok.Data;

import java.math.BigInteger;

@Data
public class PasswordChanger {
    private BigInteger userID;
    private String originalPassword;
    private String newPassword;
}

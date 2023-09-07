package com.digitalfactory.bookstore.service.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Mohamed Adel
 * User Type */
public enum UserType {

    CUSTOMER("CU"),
    ADMIN("AD");

    @Getter
    private String value;

    UserType(String value) {
        this.value = value;
    }

    public static UserType getUserType(String value) {
        return Arrays.stream(UserType.values())
                .filter(p -> Objects.equals(p.value, value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserType: ValueNotFound"));
    }
}

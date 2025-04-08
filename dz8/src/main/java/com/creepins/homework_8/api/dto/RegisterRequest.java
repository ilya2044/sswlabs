package com.creepins.homework_8.api.dto;

import com.creepins.homework_8.core.model.value.Address;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private Address address;
    private String username;
    private String password;
}
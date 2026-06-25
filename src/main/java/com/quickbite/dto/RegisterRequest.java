package com.quickbite.dto;

import com.quickbite.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private User.Role role;
}

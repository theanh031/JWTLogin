package com.example.apitoregisterphone.common.Request;

import com.example.apitoregisterphone.models.users.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Role role;
}

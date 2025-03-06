package com.example.GreetingApp.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {

    @NotBlank(message="First name is required")
    private String firstName;

    @NotBlank(message="Last name is required")
    private String lastName;

    @Email (message="Invalid email format")
    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="Password is required")
    @Pattern(
            regexp="^(?=.*[A-Za-z])(?=.\\d)[A-Za-z\\d@$!%#?&]{6,}$",
            message= "Password length should  be minimum 6 and contain at least one letter and one number"
    )

    private  String password;

}

package com.example.social.media.Dto.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegUserDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Username cannot be blank")
    private String password;

    @NotBlank(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Phone number is invalid")
    private String phone;
}

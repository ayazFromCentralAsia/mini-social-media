package com.example.social.media.Dto.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivateUserDto {
    @NotBlank(message = "Email cannot be blank")
    String email;

    @NotBlank(message = "Code cannot be blank")
    String code;
}

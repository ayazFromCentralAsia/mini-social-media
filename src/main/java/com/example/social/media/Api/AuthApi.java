package com.example.social.media.Api;


import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.User.ActivateUserDto;
import com.example.social.media.Dto.User.RegUserDto;
import com.example.social.media.Dto.User.SignInDto;
import com.example.social.media.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @PostMapping("/register")
    public SimpleResponse registerUser(@RequestBody RegUserDto userDto){
        try {
            return authService.register(userDto);
        }catch (Exception e){
            return SimpleResponse.builder().message(e.getMessage()).build();
        }
    }

    @PostMapping("/verifyEmail")
    public SimpleResponse loginUser(@RequestBody ActivateUserDto userActivate){
        return authService.activateUser(userActivate);
    }

    @PostMapping("/login")
    public SimpleResponse loginUser(@RequestBody SignInDto userDto) {
        return authService.login(userDto);
    }
}

package com.example.social.media.Service;


import com.example.social.media.Config.JWT.JWTService;
import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.User.ActivateUserDto;
import com.example.social.media.Dto.User.RegUserDto;
import com.example.social.media.Dto.User.SignInDto;
import com.example.social.media.Entities.Activation_code;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.ActivationCodeRepository;
import com.example.social.media.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    private final ActivationCodeRepository activationCodeRepository;
    private final JWTService jwtService;

    ModelMapper modelMapper = new ModelMapper();

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public SimpleResponse register(RegUserDto userDto){
        logger.info("Registering user with email " + userDto.getEmail());
        if(userRepository.existsByName(userDto.getName())){
            logger.info("User with name " + userDto.getName() + " already exists");
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("User with name " + userDto.getName() + " already exists").build();
        }
        if(userRepository.existsByEmail(userDto.getEmail())) {
            logger.info("User with this email already exists");
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("User with email " + userDto.getEmail() + " already exists").build();
        }
        if(userDto.getPassword().length() < 8){
            logger.info("Password must be at least 8 characters long");
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Password must be at least 8 characters long").build();
        }
        User user = modelMapper.map(userDto, User.class);
        user.setIs_active(false);
        Activation_code code = new Activation_code();
        code.setEmail(user.getEmail());
        code.setUser(user);
        code.setCode(sendVerificationCode(userDto.getEmail()));
        userRepository.save(user);
        activationCodeRepository.save(code);
        logger.info("User with email " + userDto.getEmail() + " inserted successfully");
        return SimpleResponse.builder().status(HttpStatus.CREATED).message("User registered successfully").build();
    }

    public SimpleResponse activateUser(ActivateUserDto activateUserDto){
        logger.info("Activating user with email " + activateUserDto.getEmail());
        User user = userRepository.findByEmail(activateUserDto.getEmail()).orElseThrow(() -> new RuntimeException("User with this email not found"));
        if(user.getIs_active()) {
            logger.info("User with email " + activateUserDto.getEmail() + " is already active");
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("User is already active").build();
        }
        Activation_code activation_code;
        try {
            activation_code = activationCodeRepository.findActivationCodeByCode(activateUserDto.getCode()).orElseThrow(() -> new RuntimeException("Activation code not found, AuthService"));
        }catch (Exception e) {
            logger.info("Invalid activation code for user with email " + activateUserDto.getEmail());
            return SimpleResponse.builder().status(HttpStatus.CONFLICT).message("Invalid activation code").build();
        }
        if (!activation_code.getEmail().equals(activateUserDto.getEmail())){
        logger.info("Invalid activation code for user with email " + activateUserDto.getEmail());
        return SimpleResponse.builder().status(HttpStatus.CONFLICT).message("Invalid activation code").build();
        }
        if(activation_code.getCode().equals(activateUserDto.getCode())){
            user.setIs_active(true);
            userRepository.save(user);
            logger.info("User with email " + activateUserDto.getEmail() + " activated successfully");
            return SimpleResponse.builder().status(HttpStatus.CREATED).message("User activated successfully").build();
        }else {
            logger.info("Invalid activation code for user with email " + activateUserDto.getEmail());
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Invalid activation code").build();
        }
    }

    public SimpleResponse login(SignInDto signInDto){
        logger.info("Logging in user with email " + signInDto.getEmail());
        User user = userRepository.findByEmail(signInDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found, AuthService, Login"));
        if(user.getIs_active()){
            if(user.getPassword().equals(signInDto.getPassword())) {
                logger.info("User with email " + signInDto.getEmail() + " logged in successfully");
                return SimpleResponse.builder().status(HttpStatus.OK).message("User logged in successfully, your token - " + jwtService.generateToken(signInDto.getEmail())).build();
            }else {
                logger.info("Invalid password for user with email " + signInDto.getEmail());
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Invalid password").build();
            }
        }
        else {
            logger.info("User with email " + signInDto.getEmail() + " is not active");
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("User is not active").build();
        }
    }

    public static String sendVerificationCode(String to) {
        logger.info("Sending verification code to " + to);
        String from = "wiliamraartur@gmail.com";

        final String password = "ebty dcdn cggt uhuw";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Random random = new Random();
            int code = 1000000 + random.nextInt(900000);
            message.setSubject("Insert this code to Form");
            message.setText(String.valueOf(code));

            Transport.send(message);
            logger.info("Verification code sent to " + to);
            return String.valueOf(code);
        }catch (MessagingException e){
            logger.error("Error sending verification code to " + to, e);
            e.printStackTrace();
        }
        return null;
    }
}

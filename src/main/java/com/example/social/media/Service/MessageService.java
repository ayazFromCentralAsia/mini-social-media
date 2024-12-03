package com.example.social.media.Service;


import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Message.MessageRequestDto;
import com.example.social.media.Dto.Message.MessagesDto;
import com.example.social.media.Entities.Messages;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.MessagesRepository;
import com.example.social.media.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    ModelMapper modelMapper = new ModelMapper();
    public SimpleResponse sendMessage(MessageRequestDto messageRequestDto){
        try {
            logger.info("Sending message to user with id: " + messageRequestDto.getReceiver());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (messageRequestDto.getReceiver() == null || messageRequestDto.getMessage().isBlank()) {
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Invalid input").build();
            }
            if (!userRepository.existsById(messageRequestDto.getReceiver())) {
                return SimpleResponse.builder().status(HttpStatus.NOT_FOUND).message("Receiver not found").build();
            }
            User sender = userRepository.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found, MessageService send message"));
            User receiver = userRepository.findById(messageRequestDto.getReceiver()).orElseThrow(() -> new RuntimeException("User not found, MessageService send message"));

            if (messageRequestDto.getReceiver().equals(sender.getId())){
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Cannot send message to self").build();
            }
            Messages messages = new Messages();
            messages.setSender(sender);
            messages.setReceiver(receiver);
            messages.setContent(messageRequestDto.getMessage());
            messages.setSentAt(LocalDateTime.now());
            messagesRepository.save(messages);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Message sent successfully").build();
        }catch (RuntimeException e){
            logger.error("Error occurred while sending message: " + e.getMessage());
            return SimpleResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Error occurred while sending message").build();
        }
    }

    public List<MessagesDto> getMessages(UUID userId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found, MessageService get messages"));
            if (userId == null) {
                throw new RuntimeException("Invalid input, MessageService get messages");
            }
            if (!userRepository.existsById(userId)) {
                throw new RuntimeException("User not found, MessageService get messages");
            }
            if (user.getId().equals(userId)) {
                throw new RuntimeException("Cannot get messages for self, MessageService get messages");
            }
            User receiver = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found, MessageService get messages"));
            List<Messages> listMessages = messagesRepository.findBySenderAndReceiver(user, receiver);
            List<MessagesDto> messagesDtoList = new ArrayList<>();
            for (Messages messages : listMessages) {
                MessagesDto messagesDto = new MessagesDto();
                messagesDto.setId(messages.getId());
                messagesDto.setSender(messages.getSender().getId());
                messagesDto.setReceiver(messages.getReceiver().getId());
                messagesDto.setContent(messages.getContent());
                messagesDto.setSentAt(messages.getSentAt());
                messagesDtoList.add(messagesDto);
            }
            return messagesDtoList;
        }catch (RuntimeException e) {
            logger.error("Error occurred while getting messages: " + e.getMessage());
            throw new RuntimeException("Error occurred while getting messages");
        }
    }

    public SimpleResponse deleteMessage(UUID messageId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found, MessageService delete message"));
            if (messageId == null) {
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Invalid input").build();
            }
            if (!messagesRepository.existsById(messageId)) {
                return SimpleResponse.builder().status(HttpStatus.NOT_FOUND).message("Message not found").build();
            }
            Messages messages = messagesRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found, MessageService delete message"));
            if (!messages.getSender().getId().equals(user.getId())) {
                return SimpleResponse.builder().status(HttpStatus.FORBIDDEN).message("Forbidden, cannot delete message").build();
            }
            messagesRepository.delete(messages);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Message deleted successfully").build();
        }catch (Exception e){
            logger.error("Error occurred while deleting message: " + e.getMessage());
            return SimpleResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Error occurred while deleting message").build();
        }
    }

    public SimpleResponse changeMessage(UUID id, MessageRequestDto messageDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            User user = userRepository.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found, MessageService change message"));
            if (id == null || messageDto.getMessage().isBlank()) {
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Invalid input").build();
            }
            if (!messagesRepository.existsById(id)) {
                return SimpleResponse.builder().status(HttpStatus.NOT_FOUND).message("Message not found").build();
            }
            Messages messages = messagesRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found, MessageService change message"));
            if (!messages.getSender().getId().equals(user.getId())) {
                return SimpleResponse.builder().status(HttpStatus.FORBIDDEN).message("Forbidden, cannot change message").build();
            }
            messages.setContent(messageDto.getMessage());
            messagesRepository.save(messages);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Message changed successfully").build();
        }catch (Exception e){
            logger.error("Error occurred while changing message: " + e.getMessage());
            return SimpleResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Error occurred while changing message").build();
        }
    }
}

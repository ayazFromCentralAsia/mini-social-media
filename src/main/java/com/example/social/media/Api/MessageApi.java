package com.example.social.media.Api;

import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Message.MessageRequestDto;
import com.example.social.media.Dto.Message.MessagesDto;
import com.example.social.media.Entities.Messages;
import com.example.social.media.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageApi {

    private final MessageService messageService;

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse send(@RequestBody MessageRequestDto messageDto) {
        return messageService.sendMessage(messageDto);
    }

    @PostMapping("/getAll/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public List<MessagesDto> send(@PathVariable("id") UUID id) {
        return messageService.getMessages(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse delete(@PathVariable("id") UUID id) {
        return messageService.deleteMessage(id);
    }

    @PutMapping("/changeMessage/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse changeMessage(@PathVariable("id") UUID id, @RequestBody MessageRequestDto messageDto) {
        return messageService.changeMessage(id, messageDto);
    }
}

package com.example.social.media.Dto.Message;

import com.example.social.media.Entities.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class MessagesDto {
    private UUID id;
    private UUID sender;
    private UUID receiver;
    private String content;
    private LocalDateTime sentAt;
}

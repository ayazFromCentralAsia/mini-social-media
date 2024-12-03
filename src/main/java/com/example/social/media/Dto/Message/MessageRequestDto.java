package com.example.social.media.Dto.Message;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MessageRequestDto {
    private UUID receiver;
    private String message;
}

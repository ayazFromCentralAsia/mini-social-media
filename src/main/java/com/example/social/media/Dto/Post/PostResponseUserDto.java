package com.example.social.media.Dto.Post;

import com.example.social.media.Entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class PostResponseUserDto {
    private UUID id;

    private String content;

    private String image_url;

    private LocalDateTime created_at;
}

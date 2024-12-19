package com.example.social.media.Dto.Post;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScrollPostResponse {
    private String content;

    private String image_url;

    private LocalDateTime created_at;
}

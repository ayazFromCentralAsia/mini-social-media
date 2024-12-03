package com.example.social.media.Dto.Post;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private String content;
    private String image_url;
}
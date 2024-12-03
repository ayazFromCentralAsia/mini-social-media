package com.example.social.media.Dto.Comment;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentRequestDto {
    UUID postId;

    String commentText;
}

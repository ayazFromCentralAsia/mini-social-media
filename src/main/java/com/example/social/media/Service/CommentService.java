package com.example.social.media.Service;


import com.example.social.media.Dto.Comment.CommentRequestDto;
import com.example.social.media.Dto.Comment.CommentTextDto;
import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Entities.Comments;
import com.example.social.media.Entities.Post;
import com.example.social.media.Entities.User;
import com.example.social.media.Repository.CommentsRepository;
import com.example.social.media.Repository.PostRepository;
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

@RequiredArgsConstructor
@Service
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postsRepository;
    private final CommentsRepository commentRepository;

    ModelMapper modelMapper = new ModelMapper();

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public SimpleResponse addComment(CommentRequestDto comment){
        try {
            if (comment.getCommentText().isBlank()){
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Comment text cannot be empty").build();
            }
            logger.info("Adding comment to post with id: " + comment.getPostId());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postsRepository.findById(comment.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));
            Comments comments = new Comments();
            comments.setContent(comment.getCommentText());
            comments.setUser_id(user);
            comments.setPostId(post);
            comments.setCreated_at(LocalDateTime.now());
            logger.info("Comment added successfully");
            commentRepository.save(comments);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Comment added successfully").build();
        }catch (RuntimeException e){
            logger.error("Error while adding comment: " + e.getMessage());
            return SimpleResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Error while adding comment").build();
        }
    }

    public List<CommentRequestDto> getAll(UUID blogId) {
        try {
            logger.info("Getting all comments");
            Post post = postsRepository.findById(blogId).orElseThrow(() -> new RuntimeException("Post not found"));
            List<Comments> comments = commentRepository.findAllByPostId(post);
            List<CommentRequestDto> commentRequestDtos = new ArrayList<>();
            for (Comments comment : comments) {
                CommentRequestDto commentRequestDto = new CommentRequestDto();
                commentRequestDto.setCommentText(comment.getContent());
                commentRequestDto.setPostId(post.getId());
                commentRequestDtos.add(commentRequestDto);
            }
            return commentRequestDtos;
        }catch (Exception e){
            logger.error("Error while getting all comments: " + e.getMessage());
            return null;
        }
    }

    public SimpleResponse changeCommentInfo(UUID id, CommentTextDto commentTextDto) {
        try {
            logger.info("Changing comment with id: " + id);
            Comments comments = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
            comments.setContent(commentTextDto.getText());
            commentRepository.save(comments);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Comment updated successfully").build();
        }catch (Exception e){
            logger.error("Error while updating comment: " + e.getMessage());
            return SimpleResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Error while updating comment").build();
        }
    }

    public SimpleResponse deleteComment(UUID id) {
        try {
            logger.info("Deleting Comment with id: " + id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found"));
            logger.info("Deleting comment with id: " + id);
            Comments comments = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
            if (comments.getUser_id().getId() == user.getId()){
                commentRepository.deleteById(id);
            }
            logger.info("Comment deleted successfully");
            return SimpleResponse.builder().status(HttpStatus.OK).message("Comment deleted successfully").build();
        }catch (Exception e){
            logger.error("Error while deleting comment: " + e.getMessage());
            return SimpleResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Error while deleting comment").build();
        }
    }
}
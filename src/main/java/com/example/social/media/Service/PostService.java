package com.example.social.media.Service;

import com.example.social.media.Dto.Extra.SimpleResponse;
import com.example.social.media.Dto.Post.PostRequestDto;
import com.example.social.media.Dto.Post.PostResponseUserDto;
import com.example.social.media.Entities.Post;
import com.example.social.media.Entities.User;
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
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postsRepository;

    ModelMapper modelMapper = new ModelMapper();

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public SimpleResponse createPost(PostRequestDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (postDto.getImage_url().isEmpty()) {
            return SimpleResponse.builder().message("Image URL is required").status(HttpStatus.BAD_REQUEST).build();
        }
        String username = authentication.getName();
        logger.info("Creating post for user: " + username);
        Post post = new Post();
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, PostService.createPost"));
        post.setContent(postDto.getContent());
        post.setImage_url(postDto.getImage_url());
        post.setUser_id(user);
        post.setCreated_at(LocalDateTime.now());
        postsRepository.save(post);
        logger.info("Post created successfully for user: " + username);
        return SimpleResponse.builder().message("Post created successfully").status(HttpStatus.OK).build();
    }

    public List<PostResponseUserDto> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("Getting all posts for user: " + username);
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, PostService.getAll"));
        List<Post> posts = postsRepository.findAllByUser_Id(user);
        List<PostResponseUserDto> postResponseUserDtos = new ArrayList<>();
        for (Post post : posts) {
            PostResponseUserDto postResponseUserDto = modelMapper.map(post, PostResponseUserDto.class);
            postResponseUserDtos.add(postResponseUserDto);
        }
        logger.info("All posts retrieved successfully for user: " + username);
        return postResponseUserDtos;
    }

    public SimpleResponse changePostInfo(UUID id, PostRequestDto postRequestDto) {
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (postRequestDto.getImage_url().isBlank()){
            return SimpleResponse.builder().message("Image URL is required").status(HttpStatus.BAD_REQUEST).build();
        }
        Post post = postsRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found, PostService.changePostInfo"));
        User user = userRepository.findByName(username).orElseThrow(() -> new RuntimeException("User not found, PostService.changePostInfo"));
        if (!post.getUser_id().equals(user)) {
            return SimpleResponse.builder().message("You are not authorized to change this post").status(HttpStatus.UNAUTHORIZED).build();
        }
        post.setContent(postRequestDto.getContent());
        post.setImage_url(postRequestDto.getImage_url());
        postsRepository.save(post);
        logger.info("Post info changed successfully for user: " + username);
        return SimpleResponse.builder().message("Post info changed successfully").status(HttpStatus.OK).build();
    }

    public SimpleResponse deletePost(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("Deleting post for user: " + username);
        if (postsRepository.existsById(id)){
            if (postsRepository.findById(id).get().getUser_id().getName().equals(username)){
                postsRepository.deleteById(id);
            }
        }else {
            logger.info("Post not found, PostService.deletePost");
            return SimpleResponse.builder().message("Post not found").status(HttpStatus.NOT_FOUND).build();
        }
        return SimpleResponse.builder().message("Post deleted successfully").status(HttpStatus.OK).build();
    }
}

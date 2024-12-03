package com.example.social.media.Repository;


import com.example.social.media.Entities.Likes;
import com.example.social.media.Entities.Post;
import com.example.social.media.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LikesRepository extends JpaRepository<Likes, UUID> {
    Likes findByPostId(Post post);
    Boolean existsByPostIdAndUserId(Post post, User userId);
    Likes findByPostIdAndUserId(Post post, User userId);
}

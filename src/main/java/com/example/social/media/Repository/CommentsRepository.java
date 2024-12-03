package com.example.social.media.Repository;


import com.example.social.media.Entities.Comments;
import com.example.social.media.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, UUID> {
    List<Comments> findAllByPostId(Post postId);
}

package com.example.social.media.Repository;


import com.example.social.media.Entities.Post;
import com.example.social.media.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p WHERE p.user_id = :user_id")
    List<Post> findAllByUser_Id(@Param("user_id") User userId);
}

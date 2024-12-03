package com.example.social.media.Repository;


import com.example.social.media.Entities.Friends;
import com.example.social.media.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, UUID> {
    Boolean existsByUser(User user);
    Optional<Friends> findByUser(User user);
}

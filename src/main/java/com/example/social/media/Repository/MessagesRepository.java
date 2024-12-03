package com.example.social.media.Repository;


import com.example.social.media.Entities.Messages;
import com.example.social.media.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, UUID> {
    Optional<Messages> findBySender(User sender);
    Optional<Messages> findByReceiver(User receiver);
    List<Messages> findBySenderAndReceiver(User sender, User receiver);
}

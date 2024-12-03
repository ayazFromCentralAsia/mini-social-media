package com.example.social.media.Repository;

import com.example.social.media.Entities.Activation_code;
import com.example.social.media.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivationCodeRepository extends JpaRepository<Activation_code, UUID> {
    Optional<Activation_code> findActivationCodeByCode(String user);

    void deleteByCreatedAtBefore(LocalDateTime time);

}

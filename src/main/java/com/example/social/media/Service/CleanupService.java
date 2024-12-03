package com.example.social.media.Service;

import com.example.social.media.Repository.ActivationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CleanupService {
    private final ActivationCodeRepository activationCodeRepository;

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredEntities() {
        LocalDateTime now = LocalDateTime.now();
        activationCodeRepository.deleteByCreatedAtBefore(now);
    }
}
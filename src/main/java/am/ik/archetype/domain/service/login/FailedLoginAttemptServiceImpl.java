package am.ik.archetype.domain.service.login;

import am.ik.archetype.domain.repository.login.FailedLoginAttemptRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class FailedLoginAttemptServiceImpl implements FailedLoginAttemptService {
    @Autowired
    FailedLoginAttemptRepository failedLoginAttemptRepository;

    @Value("${failedloginattempt.unlock.min:1}")
    long unlockMin;

    @Override
    @Scheduled(initialDelay = 600_000, fixedRate = 600_000)
    public void cleanup() {
        log.info("Clean up ...");
        LocalDateTime now = LocalDateTime.now();
        failedLoginAttemptRepository.findAll()
                .forEach(attempt -> {
                    LocalDateTime attemptTime = attempt.getAttemptId().getAttemptTime().toLocalDateTime();
                    if (now.isAfter(attemptTime.plusMinutes(unlockMin))) {
                        failedLoginAttemptRepository.delete(attempt);
                    }
                });
        log.info("Done");
    }
}

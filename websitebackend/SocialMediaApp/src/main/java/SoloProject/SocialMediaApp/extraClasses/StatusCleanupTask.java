package SoloProject.SocialMediaApp.extraClasses;

import SoloProject.SocialMediaApp.models.Status;
import SoloProject.SocialMediaApp.repository.StatusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatusCleanupTask {

    @Autowired
    private StatusRepository statusRepository;

    @Scheduled(fixedRate = 100000)
    public void cleanupExpiredStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Status> expiredStatuses = statusRepository.findByDeathBefore(now);
        statusRepository.deleteAll(expiredStatuses);
    }
}
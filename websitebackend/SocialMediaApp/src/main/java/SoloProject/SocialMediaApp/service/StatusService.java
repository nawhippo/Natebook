package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.Status;
import SoloProject.SocialMediaApp.repository.StatusRepository;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Transactional
    public Status createStatus(Long appUserId, String content, int lifespan) {
        statusRepository.deleteByAppUserID(appUserId);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime death = start.plusHours(lifespan);
        Status newStatus = new Status(appUserId, content, start, lifespan, death);
        return statusRepository.save(newStatus);
    }

    public Status getStatusByUser(Long appUserId) {

        System.out.println(statusRepository.findByAppUserID(appUserId));
        return statusRepository.findByAppUserID(appUserId);
    }

}
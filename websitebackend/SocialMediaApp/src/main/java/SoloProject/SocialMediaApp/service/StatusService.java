package SoloProject.SocialMediaApp.service;
import SoloProject.SocialMediaApp.models.Status;
import SoloProject.SocialMediaApp.repository.StatusRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;

    }
    StatusRepository statusRepository;

    @Scheduled(fixedDelay = 60000)
    public void deleteExpiredStatus() {
        List<Status> expiredStatusList;
        expiredStatusList = statusRepository.findByDeathBefore(DateTime.now());
        statusRepository.deleteAll(expiredStatusList);
    }
}

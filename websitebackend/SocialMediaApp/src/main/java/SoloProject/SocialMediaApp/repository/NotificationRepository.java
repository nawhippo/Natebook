package SoloProject.SocialMediaApp.repository;
import SoloProject.SocialMediaApp.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.objectId = :objectId")
    long countByUserIdAndObjectId(@Param("userId") Long userId, @Param("objectId") Long objectId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.objectId = :objectId AND n.userId = :userId AND n.notificationType = :type")
    void deleteByObjectIdAndUserIdAndType(@Param("objectId") Long objectId,
                                          @Param("userId") Long userId,
                                          @Param("type") String type);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.notificationType = :type")
    long countByUserIdAndNotificationType(@Param("userId") Long userId,
                                          @Param("type") String type);


    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.objectId = :objectId")
    List<Notification> findByUserIdAndObjectId(@Param("userId") Long userId,
                                               @Param("objectId") Long objectId);


    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.notificationType = :type")
    List<Notification> findByUserIdAndNotificationType(@Param("userId") Long userId,
                                                       @Param("type") String type);

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.objectId = :objectId AND n.notificationType = :type")
    List<Notification> findByUserIdAndObjectIdAndNotificationType(@Param("userId") Long userId,
                                                                  @Param("objectId") Long objectId,
                                                                  @Param("type") String type);

    List<Notification> findByNotificationType(String notificationType);

    int countByUserIdAndThreadIdAndNotificationType(Long userId, Long threadId, String notificationType);
}

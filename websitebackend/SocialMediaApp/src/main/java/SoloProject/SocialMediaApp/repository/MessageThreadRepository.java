package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageThreadRepository extends JpaRepository<MessageThread, Long> {

    @Query("SELECT t FROM MessageThread t " +
            "WHERE :participantIds MEMBER OF t.participants " +
            "GROUP BY t " +
            "HAVING COUNT(t.participants) = :participantCount " +
            "AND COUNT(t.participants) = (SELECT COUNT(tp) FROM MessageThread t2 JOIN t2.participants tp WHERE t2 = t)")
    Optional<MessageThread> findMessageThreadByExactParticipants(
            @Param("participantIds") List<Long> participantIds,
            @Param("participantCount") long participantCount
    );
}
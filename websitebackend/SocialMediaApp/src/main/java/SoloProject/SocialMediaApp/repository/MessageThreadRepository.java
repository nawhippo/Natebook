package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageThreadRepository extends JpaRepository<MessageThread, Long> {
    List<MessageThread> findByParticipantsContains(Long participantId);
    @Query("SELECT COUNT(m) FROM Message m WHERE m.threadid = :threadId AND m.senderid != :userId")
    int countMessagesInThreadByOtherUsers(Long threadId, Long userId);
}
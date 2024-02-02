package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MessageThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public MessageThread(List<Long> participants, List<String> participantsNanes) {
        this.participants = participants;
        this.participantsNames = participantsNames;
    }
    @ElementCollection
    List<Long> participants;

    @ElementCollection
    List<String> participantsNames;

    public MessageThread(List<String> participantsNames) {
        this.participantsNames = participantsNames;
    }
    public MessageThread(Long id, List<Long> participants) {
        this.id = id;
        this.participants = participants;
    }

    public void setParticipantsNames(List<String> recipientusernames) {
        this.participantsNames = recipientusernames;
    }
}

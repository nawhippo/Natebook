package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MessageThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }

    public MessageThread() {

    }

    public MessageThread(List<Long> participants, List<String> participantsNanes) {
        this.participants = participants;
        this.participantsNames = participantsNames;
    }
    @ElementCollection
    List<Long> participants;

    public List<String> getParticipantsNames() {
        return participantsNames;
    }

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

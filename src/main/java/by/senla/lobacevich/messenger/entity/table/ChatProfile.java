package by.senla.lobacevich.messenger.entity.table;

import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.entity.Profile;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "mes", name = "chats_profiles")
public class ChatProfile {

    @EmbeddedId
    private ChatProfileId id = new ChatProfileId();

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private Profile participant;
}


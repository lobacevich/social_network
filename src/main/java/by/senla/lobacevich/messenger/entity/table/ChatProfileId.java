package by.senla.lobacevich.messenger.entity.table;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ChatProfileId {
    private Long chatId;
    private Long participantId;
}

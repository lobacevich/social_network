package by.senla.lobacevich.messenger.entity.table;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class GroupParticipantId {
    private Long groupId;
    private Long participantId;
}

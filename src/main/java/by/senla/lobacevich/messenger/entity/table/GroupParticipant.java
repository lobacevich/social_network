package by.senla.lobacevich.messenger.entity.table;

import by.senla.lobacevich.messenger.entity.Group;
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
@Table(schema = "mes", name = "groups_participants")
public class GroupParticipant {

    @EmbeddedId
    private GroupParticipantId id = new GroupParticipantId();

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private Profile participant;
}


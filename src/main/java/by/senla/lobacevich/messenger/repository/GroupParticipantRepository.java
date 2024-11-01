package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.table.GroupParticipant;
import by.senla.lobacevich.messenger.entity.table.GroupParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, GroupParticipantId> {

    @Modifying
    @Query("DELETE FROM GroupParticipant gp WHERE gp.id.participantId = :profileId")
    void deleteByProfileId(@Param("profileId") Long profileId);
}

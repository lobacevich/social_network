package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.table.ChatProfile;
import by.senla.lobacevich.messenger.entity.table.ChatProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatProfileRepository extends JpaRepository<ChatProfile, ChatProfileId> {

    @Modifying
    @Query("DELETE FROM ChatProfile cp WHERE cp.id.participantId = :profileId")
    void deleteByProfileId(@Param("profileId") Long profileId);
}

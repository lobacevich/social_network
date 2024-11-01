package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @EntityGraph(attributePaths = {"participants", "messages", "owner"})
    @Override
    Optional<Chat> findById(Long aLong);

    @EntityGraph(attributePaths = {"participants", "messages", "owner"})
    @Query("SELECT c FROM Chat c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Chat> searchChats(@Param("name") String name, Pageable pageable);

    @EntityGraph(attributePaths = {"participants", "messages", "owner"})
    @Query("SELECT c FROM Chat c WHERE c.owner.id = :ownerId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Chat> findByOwnerIdAndNameLike(@Param("ownerId") Long ownerId, @Param("name") String name, Pageable pageable);

    @EntityGraph(attributePaths = {"participants", "messages", "owner"})
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.id = :participantId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Chat> findByParticipantIdAndNameLike(@Param("participantId") Long participantId, @Param("name") String name, Pageable pageable);

}

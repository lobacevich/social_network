package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.RequestFriendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestFriendshipRepository extends JpaRepository<RequestFriendship, Long> {

    @EntityGraph(attributePaths = {"sender", "recipient"})
    @Override
    Optional<RequestFriendship> findById(Long aLong);

    @EntityGraph(attributePaths = {"sender", "recipient"})
    @Override
    Page<RequestFriendship> findAll(Pageable pageable);

    Optional<RequestFriendship> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}

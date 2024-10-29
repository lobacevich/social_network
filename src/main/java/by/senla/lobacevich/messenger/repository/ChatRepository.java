package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @EntityGraph(attributePaths = {"participants", "messages"})
    @Override
    Optional<Chat> findById(Long aLong);

    @EntityGraph(attributePaths = {"participants", "messages"})
    @Override
    Page<Chat> findAll(Pageable pageable);
}

package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = {"chat", "author"})
    @Override
    Optional<Message> findById(Long aLong);

    @EntityGraph(attributePaths = {"chat", "author"})
    @Override
    Page<Message> findAll(Pageable pageable);
}

package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

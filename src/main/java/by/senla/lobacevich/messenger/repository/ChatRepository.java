package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}

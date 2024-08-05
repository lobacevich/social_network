package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

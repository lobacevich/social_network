package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

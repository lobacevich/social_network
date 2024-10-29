package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"post", "author", "post.author", "post.group"})
    @Override
    Optional<Comment> findById(Long aLong);

    @EntityGraph(attributePaths = {"post", "author", "post.author", "post.group"})
    @Override
    Page<Comment> findAll(Pageable pageable);
}

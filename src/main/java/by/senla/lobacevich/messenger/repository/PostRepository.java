package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "group", "comments"})
    @Override
    Optional<Post> findById(Long aLong);

    @EntityGraph(attributePaths = {"author", "group", "comments"})
    @Override
    Page<Post> findAll(Pageable pageable);
}

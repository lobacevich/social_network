package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @EntityGraph(attributePaths = {"owner", "participants", "posts"})
    @Override
    Optional<Group> findById(Long aLong);

    @EntityGraph(attributePaths = {"owner", "participants", "posts"})
    @Override
    Page<Group> findAll(Pageable pageable);

    @Query("SELECT g FROM Group g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Group> searchGroups(@Param("name") String name, Pageable pageable);
}

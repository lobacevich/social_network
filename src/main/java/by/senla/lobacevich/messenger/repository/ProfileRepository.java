package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @EntityGraph(attributePaths = {"user", "groups", "ownedGroups", "chats"})
    @Override
    Optional<Profile> findById(Long aLong);

    @EntityGraph(attributePaths = {"user", "groups", "ownedGroups", "chats"})
    @Override
    Page<Profile> findAll(Pageable pageable);

    @Query("SELECT p FROM Profile p JOIN p.user u WHERE u.username = :username")
    Optional<Profile> findByUsername(@Param("username") String username);
}

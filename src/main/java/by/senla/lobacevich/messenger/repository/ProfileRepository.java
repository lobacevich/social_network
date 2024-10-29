package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @EntityGraph(attributePaths = {"user", "groups", "ownedGroups", "chats"})
    @Override
    Optional<Profile> findById(Long aLong);

    @EntityGraph(attributePaths = {"user", "groups", "ownedGroups", "chats"})
    @Override
    Page<Profile> findAll(Pageable pageable);
}

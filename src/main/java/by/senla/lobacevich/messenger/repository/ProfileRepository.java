package by.senla.lobacevich.messenger.repository;

import by.senla.lobacevich.messenger.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}

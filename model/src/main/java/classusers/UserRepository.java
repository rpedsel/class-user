package classusers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLastname(String lastname);
    Collection<User> findStudiedclassesByLastname(String lastname);
}

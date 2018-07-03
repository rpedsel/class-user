package classusers;

import java.util.List;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLastname(String lastname);
    List<User> findAll();
    //Collection<User> findStudiedclassesByLastname(String lastname);
}

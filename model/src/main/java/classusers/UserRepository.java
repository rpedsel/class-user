package classusers;

import java.util.List;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Collection<User> findByFirstname(String firstname);
    Collection<User> findByLastname(String lastname);
    Collection<User> findByEmail(String email);
}

package classusers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Collection;

public interface EClassRepository extends JpaRepository<EClass, Long> {
    Optional<EClass> findByClassname(String classname);
    Collection<EClass> findByCreatorLastname(String lastname);
    //Collection<User> findStudentsByClassname(String classname);
}

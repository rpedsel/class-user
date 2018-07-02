package classusers;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface EClassRepository extends JpaRepository<EClass, Long> {
    Collection<EClass> findByCreatorLastname(String lastname);
}

package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Optional<Type> findById(long id);
    Optional<Type> findByNameIgnoreCase(String name);

    @Query("SELECT t.name FROM Type t WHERE t.id = ?1")
    Optional<String> findNameById(long id);
}

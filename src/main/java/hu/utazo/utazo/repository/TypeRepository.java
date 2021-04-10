package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {


    Optional<Type> findByNameIgnoreCase(String name);
}

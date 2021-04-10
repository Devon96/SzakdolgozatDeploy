package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContinentRepository extends JpaRepository<Continent, Long> {
    Continent findByName(String name);
}

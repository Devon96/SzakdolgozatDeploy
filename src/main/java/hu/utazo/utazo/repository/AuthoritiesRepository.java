package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Authorities;
import hu.utazo.utazo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
    Authorities findByName(String name);
}

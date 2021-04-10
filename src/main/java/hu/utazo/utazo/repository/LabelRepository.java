package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Label;
import hu.utazo.utazo.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Label findByName(String name);

    @Query("SELECT l.id FROM Label l ORDER BY l.id DESC ")
    long finTopId( PageRequest pageRequest );
}

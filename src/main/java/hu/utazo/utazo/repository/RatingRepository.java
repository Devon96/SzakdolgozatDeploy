package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.Rating;
import hu.utazo.utazo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByAttractionAndUser(Attraction attraction, User user);

    @Query("SELECT r.rating AS rating, COUNT(r.id) AS count FROM Rating AS r WHERE r.attraction.id = ?1 GROUP BY r.rating ORDER BY r.rating DESC")
    List<int[]> getRatingsToTemplate(long attractionId);

    @Query(value = "(SELECT 1 , COUNT(*) FROM Rating AS r WHERE r.attraction_id = ?1 AND r.rating = 1) UNION (SELECT 2 , COUNT(*) FROM Rating AS r WHERE r.attraction_id = ?1 AND r.rating = 2) UNION (SELECT 3 , COUNT(*) FROM Rating AS r WHERE r.attraction_id = ?1 AND r.rating = 3) UNION (SELECT 4 , COUNT(*) FROM Rating AS r WHERE r.attraction_id = ?1 AND r.rating = 4) UNION (SELECT 5 , COUNT(*) FROM Rating AS r WHERE r.attraction_id = ?1 AND r.rating = 5)", nativeQuery = true)
    List<int[]> getRatingsToTemplate2(long attractionId);
}

package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.SavedAttraction;
import hu.utazo.utazo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedAttractionRepository extends JpaRepository<SavedAttraction, Long> {

    void deleteByAttractionAndUser(Attraction attraction, User user);

    //@Query("SELECT a FROM Attraction a, City ci, SavedAttraction sa, Country co WHERE a.city.id = ci.id AND ci.country.id = ?1 order by a.city.country desc")
    //List<Attraction> getAttractionsOrderedByCountry(String username);

    @Query("SELECT sa.attraction FROM SavedAttraction sa WHERE sa.user.email = ?1 or sa.user.googleId = ?1 order by sa.attraction.city.country.name desc")
    List<Attraction> getAttractionsOrderedByCountry(String username);

    @Query("SELECT sa FROM SavedAttraction sa WHERE ( sa.user.email = ?1 or sa.user.googleId = ?1 ) AND sa.attraction.id = ?2")
    Optional<SavedAttraction> findFirstByUserIdAndAttraction(String userId, long attractionId);

}

package hu.utazo.utazo.repository;

import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.City;
import hu.utazo.utazo.model.Country;
import hu.utazo.utazo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    Attraction findFirstByOrderByIdDesc();

    @Query("SELECT a FROM Attraction a WHERE a.city.country.id = ?1 order by a.id desc")
    List<Attraction> findBestsByCountry(String countryId, Pageable pageable);

    @Query("SELECT a FROM Attraction a WHERE a.city.country.id = ?1")
    List<Attraction> findAllByCountryId(String countryId);

    List<Attraction> findAllByCityOrderByIdDesc(City city, Pageable pageable);

    List<Attraction> findAllByCity(City city);

    Optional<Attraction> findByNameAndCity(String name, City city);

    @Query("SELECT a, AVG(r.rating) FROM Attraction a, Rating r, City c, Country co WHERE a.id = r.attraction.id AND a.city.id = c.id AND c.country.id = co.id AND co.id = ?1 GROUP BY a ORDER BY AVG(r.rating) DESC")
    List<Attraction> findTopAttractionsInTheCountry( String countryId, Pageable pageable );

    @Query(value ="SELECT * FROM Attraction as a, City WHERE a.city_id = City.id AND City.country_id = ?2 AND lower(a.name) LIKE lower(?1) ORDER BY ( SELECT AVG(Rating.rating) FROM Rating WHERE Rating.attraction_id = a.id ) DESC ", nativeQuery = true)
    Page<Attraction> findAllAttractionsInCountry(String name, String countryId, PageRequest of);

    @Query(value ="SELECT * FROM Attraction as a, City WHERE a.city_id = City.id AND City.country_id = ?2 AND lower(a.name) LIKE lower(?1) ORDER BY (SELECT COUNT(*) FROM Attraction_labels WHERE Attraction_labels.attraction_id = a.id AND Attraction_labels.label_id in ( SELECT Attraction_labels.label_id FROM Saved_attraction, Attraction attr, Attraction_labels WHERE attr.id = Saved_attraction.attraction_id AND attr.id = Attraction_labels.attraction_id AND Saved_attraction.user_id = ?3 )) DESC ", nativeQuery = true)
    List<Attraction> findAllProposalAttractionsInCountry(String name, String countryId, long userId, PageRequest of);

    @Query("SELECT COUNT(a) FROM Attraction a WHERE a.name LIKE ?1 AND a.city.country.id = ?2")
    long countFindAllProposalAttractionsInCountry(String name, String countryId);

    //@Query("SELECT a, AVG(r.rating) FROM Attraction a, Rating r, City c WHERE a.id = r.attraction.id AND a.city.id = c.id AND c.id = ?1 GROUP BY a ORDER BY AVG(r.rating) DESC")
    @Query(value ="SELECT * FROM Attraction as a WHERE lower(a.name) LIKE lower(?1) AND a.city_id = ?2 ORDER BY (SELECT AVG(Rating.rating) FROM Rating WHERE Rating.attraction_id = a.id) DESC ", nativeQuery = true)
    Page<Attraction> findTopAttractionsInTheCity(String name, long cityId, Pageable pageable );

    @Query(value ="SELECT * FROM Attraction as a WHERE a.city_id = ?2 AND lower(a.name) LIKE lower(?1) ORDER BY (SELECT COUNT(*) FROM Attraction_labels WHERE Attraction_labels.attraction_id = a.id AND Attraction_labels.label_id in ( SELECT Attraction_labels.label_id FROM Saved_attraction, Attraction attr, Attraction_labels WHERE attr.id = Saved_attraction.attraction_id AND attr.id = Attraction_labels.attraction_id AND Saved_attraction.user_id = ?3 )) DESC ", nativeQuery = true)
    List<Attraction> findAllSuggestionAttractionsInCity(String name, long cityId, long userId, PageRequest of);

    @Query("SELECT COUNT(a) FROM Attraction a WHERE lower(a.name) LIKE lower(?1) AND a.city.id = ?2 ")
    long countFindAllSuggestionAttractionsInCity(String name, long cityId);
}

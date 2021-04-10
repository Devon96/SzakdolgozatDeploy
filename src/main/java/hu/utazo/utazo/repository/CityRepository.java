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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findByCountryAndName(Country country, String name);

    ArrayList<City> findAllByCountry(Country country);

    @Query("SELECT a.country FROM City a WHERE a.id = ?1")
    Country findCountryIdByCityId(long id);

    @Query("SELECT ci, COUNT(sa) FROM SavedAttraction sa, Attraction a, Country co, City ci WHERE sa.attraction.id = a.id AND a.city.id = ci.id AND ci.country.id = co.id AND co.id = ?1 GROUP BY ci ORDER BY COUNT(sa) DESC")
    List<City> findTopCitiesInTheCountry(String countryId, Pageable pageable);

    List<City> findFirst10ByOrderById();

    List<City> findFirst10ByCountry(Country country);

    ArrayList<City> findFirst5ByNameLike(String name);

    ArrayList<City> findAllByCountry(Country country, Pageable pageable);

    @Query("SELECT sa.attraction.city FROM SavedAttraction sa WHERE sa.user = ?1 GROUP BY sa.attraction.city ORDER BY COUNT(sa) DESC")
    List<City> findCitiesForMe(User user, PageRequest pageRequest);

    @Query("SELECT ci, COUNT(sa) FROM SavedAttraction sa, Attraction a, City ci WHERE sa.attraction.id = a.id AND a.city.id = ci.id GROUP BY ci ORDER BY COUNT(sa) DESC")
    List<City> findPopularCities(PageRequest pageRequest);

    @Query("SELECT ci.id FROM City ci WHERE ci.name = ?1 AND ci.country.name = ?2")
    Optional<Long> findCytyIdByNameAndCountryName(String cityName, String countryName);

    @Query(value ="SELECT * FROM City as ci WHERE lower(ci.name) LIKE lower(?1) ORDER BY ( SELECT COUNT(*) FROM City, Attraction, Saved_attraction WHERE City.id = Attraction.city_id AND Saved_attraction.attraction_id = Attraction.id AND Saved_attraction.user_id = ?2 AND City.id = ci.id ) DESC ", nativeQuery = true)
    List<City> findAllCitiesFirstVisit(String cityName, long userId, PageRequest pageRequest);

    @Query("SELECT count(ci) FROM City ci WHERE ci.name LIKE ?1")
    long countFindAllCitiesFirstVisit(String name);


    @Query(value ="SELECT * FROM City as ci WHERE lower(ci.name) LIKE lower(?1) AND ci.country_id = ?2 ORDER BY ( SELECT COUNT(*) FROM City, Attraction, Saved_attraction WHERE City.id = ci.id AND City.id = Attraction.city_id AND Attraction.id = Saved_attraction.attraction_id) DESC ", nativeQuery = true)
    Page<City> findAllCitiesInCountry(String cityName, String countryId, PageRequest pageRequest);
}

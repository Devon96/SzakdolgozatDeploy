package hu.utazo.utazo.repository;

import ch.qos.logback.core.boolex.EvaluationException;
import hu.utazo.utazo.model.City;
import hu.utazo.utazo.model.Country;
import hu.utazo.utazo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {


    //@Query("SELECT un FROM Country un ORDER BY un.id LIMIT 5")
    List<Country> findFirst10ByOrderById();

    ArrayList<Country> findFirst5ByNameLike(String name);

    @Query("SELECT co, COUNT(sa) FROM SavedAttraction sa, Attraction a, Country co, City ci WHERE sa.attraction.id = a.id AND a.city.id = ci.id AND ci.country.id = co.id AND sa.user = ?1 GROUP BY co ORDER BY COUNT(sa) DESC")
    List<Country> countriesForMe(User user, PageRequest pageRequest);

    @Query("SELECT co, COUNT(sa) FROM SavedAttraction sa, Attraction a, Country co, City ci WHERE sa.attraction.id = a.id AND a.city.id = ci.id AND ci.country.id = co.id GROUP BY co ORDER BY COUNT(sa) DESC")
    List<Country> popularCountries();

    @Query("SELECT ci.country.id FROM City ci WHERE ci.id = ?1")
    String getCountyIdByCityId(long cityId);

    @Query(value ="SELECT * FROM Country as co WHERE lower(co.name) LIKE lower(?1) ORDER BY ( SELECT COUNT(*) FROM Country cou, City, Attraction, Saved_attraction WHERE cou.id = City.country_id AND City.id = Attraction.city_id AND Saved_attraction.attraction_id = Attraction.id AND (Saved_attraction.user_id = ?2) AND cou.id = co.id ) DESC ", nativeQuery = true)
    List<Country> findCountryPage(String countryName, long userid, PageRequest pageRequest);

    @Query("SELECT count(co) FROM Country co WHERE co.name LIKE ?1")
    long countFindCountryPage(String name);

}




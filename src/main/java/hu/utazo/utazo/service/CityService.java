package hu.utazo.utazo.service;

import hu.utazo.utazo.model.*;
import hu.utazo.utazo.repository.AttractionRepository;
import hu.utazo.utazo.repository.CityRepository;
import hu.utazo.utazo.repository.CountryRepository;
import hu.utazo.utazo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CityService {

    CityRepository cityRepository;
    AttractionRepository attractionRepository;
    UserRepository userRepository;
    CountryRepository countryRepository;


    @Autowired
    public CityService(CityRepository cityRepository, AttractionRepository attractionRepository, UserRepository userRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.attractionRepository = attractionRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }

    public ArrayList<City> findAllByCountry(String id){
        Country country = new Country();
        country.setId(id);
        return cityRepository.findAllByCountry(country);
    }

    public void save(City city, MultipartFile countryImage) throws IOException {

        cityRepository.save(city);

        city = cityRepository.findByCountryAndName(city.getCountry(), city.getName());

        byte[] imageBytes = countryImage.getBytes();
        Path path = Paths.get("src/main/resources/static/photos/" + city.getCountry().getId() + "/" + city.getId());
        Files.createDirectories(path);
        Path path2 = Paths.get("src/main/resources/static/photos/" + city.getCountry().getId() + "/" + city.getId() + "/city.jpg");

        try{
            Files.createFile(path2);
        }catch (FileAlreadyExistsException e){
            System.out.println("Már létezik");
        }
        Files.write(path2, imageBytes);
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public List<City> findFirst10ByOrderById() {


        return cityRepository.findFirst10ByOrderById();


    }

    public ArrayList<City> findFirst5ByNameLike(String name){
        return cityRepository.findFirst5ByNameLike(name + "%");
    }

    public City findById(long cityId) {
        City city = cityRepository.findById(cityId).orElse(null);
        if(city == null){
            return null;
        }

        ////ajánlott látnivalók////
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(user == null || user.getAttractions().isEmpty()){
            city.setSuggestAttractions(null);
        }else{
            fillOfSuggestAttractions(user, city);
        }

        ////Top Látnivalók////
        city.setBestAttractions(attractionRepository.findTopAttractionsInTheCity("%", city.getId(), PageRequest.of(0,10)).getContent());

        return city;
    }


    private void fillOfSuggestAttractions(User user, City city){

        Set<Label> kedveltCimkek = new HashSet<>();
        Set<Attraction> kedveltLatnivalok = new HashSet<>();


        for(SavedAttraction sa : user.getAttractions()){
            kedveltCimkek.addAll(sa.getAttraction().getLabels());
            kedveltLatnivalok.add(sa.getAttraction());
        }

        Map<Attraction, Double> attractionMap = new HashMap<>();

        for( Attraction a : city.getAttractions() ){
            Set<Label> tempSet = new HashSet<>(kedveltCimkek);
            tempSet.addAll(Set.copyOf(a.getLabels()));

            int osszeg = kedveltCimkek.size() + a.getLabels().size();
            int egyesitettOsszeg = tempSet.size();

            attractionMap.put(a, osszeg / (double) egyesitettOsszeg);
            

        }

        city.setSuggestAttractions(new ArrayList<>());

        for(int i = 0; i < 10 && attractionMap.size() > kedveltLatnivalok.size(); i++){

            Attraction a = null;
            double max = Double.MIN_VALUE;
            for(Map.Entry<Attraction, Double> entry : attractionMap.entrySet()){
                if(entry.getValue() > max && ! kedveltLatnivalok.contains(entry.getKey())){
                    a = entry.getKey();
                    max = entry.getValue();
                }
            }

            city.getSuggestAttractions().add(a);
            attractionMap.remove(a);
        }

    }

    public List<City> findPopularCities() {

        List<City> cities = new ArrayList<>();

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if(user != null){
            cities = cityRepository.findCitiesForMe(user, PageRequest.of(0,10));
        }

        if(cities.size() < 10){
            for( City c : cityRepository.findPopularCities(PageRequest.of(0,10)) ) {
                if (!cities.contains(c)) {
                    cities.add(c);
                }
                if (cities.size() >= 10) {
                    break;
                }
            }
        }

        if(cities.size() < 10){
            for( City c : cityRepository.findAll(PageRequest.of(0,10)) ) {
                if (!cities.contains(c)) {
                    cities.add(c);
                }
                if (cities.size() >= 10) {
                    break;
                }
            }
        }

        return cities;
    }

    public List<City> findTopCitiesForMe() {

        List<City> cities = new ArrayList<>();
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if(user != null){
            for( Country c : countryRepository.countriesForMe(user, PageRequest.of(0,10)) ){
                cities.addAll(c.getAllCity());
            }
            if(cities.size() >= 10){
                Collections.shuffle(cities);
                cities = cities.subList(0,10);
                return cities;
            }
        }

        if(cities.size() < 10){
            for( City c : cityRepository.findPopularCities(PageRequest.of(0,19)) ) {
                if (!cities.contains(c)) {
                    cities.add(c);
                }
                if (cities.size() >= 10) {
                    break;
                }
            }
        }

        if(cities.size() < 10){
            for( City c : cityRepository.findAll(PageRequest.of(0,10)) ) {
                if (!cities.contains(c)) {
                    cities.add(c);
                }
                if (cities.size() >= 10) {
                    break;
                }
            }
        }

        Collections.shuffle(cities);
        try {
            cities = cities.subList(0,10);
        }catch (Exception e){
            return cities;
        }


        return cities;
    }

    public long findCytyIdByNameAndCountryName(String cityName, String countryName) throws Exception {
        long cityId = cityRepository.findCytyIdByNameAndCountryName(cityName, countryName).orElse(0L);

        if(cityId == 0){
            throw new Exception("Nincs " + cityName + " nevű város.");
        }

        return cityId;
    }


    public Page<City> allCitiesPage(int pageNumber, String cityName){

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        List<City> cities = null;
        if(user == null){
            cities = cityRepository.findAllCitiesFirstVisit( "%" + cityName + "%", 0, PageRequest.of(pageNumber,12) );
        }else{
            cities = cityRepository.findAllCitiesFirstVisit( "%" + cityName + "%", user.getId(), PageRequest.of(pageNumber,12) );
        }

        return new PageImpl<City>(cities, PageRequest.of(pageNumber,12), cityRepository.countFindAllCitiesFirstVisit("%" + cityName + "%"));
    }

    public Page<City> findAllCitiesInCountry(String cityName, String countryId, int pageNumber){
        return cityRepository.findAllCitiesInCountry( "%" + cityName + "%", countryId, PageRequest.of(pageNumber,12) );
    }


}

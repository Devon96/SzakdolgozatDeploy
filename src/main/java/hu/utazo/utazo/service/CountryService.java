package hu.utazo.utazo.service;

import hu.utazo.utazo.model.*;
import hu.utazo.utazo.repository.*;
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
public class CountryService {

CountryRepository countryRepository;
ContinentRepository continentRepository;
CityRepository cityRepository;
AttractionRepository attractionRepository;
UserRepository userRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository, ContinentRepository continentRepository, CityRepository cityRepository, AttractionRepository attractionRepository, UserRepository userRepository) {
        this.countryRepository = countryRepository;
        this.continentRepository = continentRepository;
        this.cityRepository = cityRepository;
        this.attractionRepository = attractionRepository;
        this.userRepository = userRepository;
    }

    public void save(Country country, MultipartFile countryImage) throws IOException {
        String name = country.getName();


        name = name.toLowerCase();
        name = name.replaceAll("á", "a");
        name = name.replaceAll("í", "i");
        name = name.replaceAll("ű", "u");
        name = name.replaceAll("ő", "o");
        name = name.replaceAll("ü", "u");
        name = name.replaceAll("ö", "o");
        name = name.replaceAll("ú", "u");
        name = name.replaceAll("ó", "o");
        name = name.replaceAll("é", "e");
        name = name.replaceAll(" ", "_");
        country.setId(name);


        byte[] imageBytes = countryImage.getBytes();
        Path path = Paths.get("src/main/resources/static/photos/" + country.getId());
        Files.createDirectories(path);
        Path path2 = Paths.get("src/main/resources/static/photos/" + country.getId() + "/country.jpg");
        try{
            Files.createFile(path2);
        }catch (FileAlreadyExistsException e){
            System.out.println("Már létezik");
        }

        Files.write(path2, imageBytes);

        countryRepository.save(country);
    }

    public List<Country> findAll(){
        return countryRepository.findAll();
    }

    public List<Country> findFirstByOrderById(){
        return countryRepository.findFirst10ByOrderById();
    }


    public ArrayList<Country> findFirst5ByNameLike(String name) {
        return countryRepository.findFirst5ByNameLike(name + "%");
    }

    public Country findById(String id) throws Exception {
        Country country = countryRepository.findById(id).orElse(null);
        if(country == null){
            throw new Exception("Nincs ilyen ország");
        }
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);


        ////Leg látogatottabb városok beállítása////
        country.setCities(cityRepository.findAllCitiesInCountry("%", country.getId(), PageRequest.of(0,10)).getContent());



        ////top látnivalók beállítása////
        country.setTopAttractions(attractionRepository.findAllAttractionsInCountry("%", id, PageRequest.of(0,10)).getContent());


        ////ajánlott látnivalók beállítása////
        if(user != null && !user.getAttractions().isEmpty()){
            fillOfSuggestAttractions(user, country);
        }else{
            country.setSuggestAttractions(null);
        }

        return country;
    }




    private void fillOfSuggestAttractions(User user, Country country){

        Set<Label> kedveltCimkek = new HashSet<>();
        Set<Attraction> kedveltLatnivalok = new HashSet<>();


        for(SavedAttraction sa : user.getAttractions()){
            kedveltCimkek.addAll(sa.getAttraction().getLabels());
            kedveltLatnivalok.add(sa.getAttraction());
        }

        Map<Attraction, Double> attractionMap = new HashMap<>();

        for( Attraction a : attractionRepository.findAllByCountryId(country.getId()) ){
            Set<Label> tempSet = new HashSet<>(kedveltCimkek);
            tempSet.addAll(Set.copyOf(a.getLabels()));

            int osszeg = kedveltCimkek.size() + a.getLabels().size();
            int egyesitettOsszeg = tempSet.size();

            attractionMap.put(a, osszeg / (double) egyesitettOsszeg);


        }

        country.setSuggestAttractions(new ArrayList<>());

        for(int i = 0; i < 10 && attractionMap.size() > kedveltLatnivalok.size(); i++){

            Attraction a = null;
            double max = Double.MIN_VALUE;
            for(Map.Entry<Attraction, Double> entry : attractionMap.entrySet()){
                if(entry.getValue() > max && ! kedveltLatnivalok.contains(entry.getKey())){
                    a = entry.getKey();
                    max = entry.getValue();
                }
            }

            country.getSuggestAttractions().add(a);
            attractionMap.remove(a);
        }

    }


    public List<Country> findProposalCountries() {
        List<Country> countries = new ArrayList<>();
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if(user != null){
            countries = countryRepository.countriesForMe(user, PageRequest.of(0, 10));
        }

        if(countries.size() < 10){

            for( Country c : countryRepository.popularCountries() ){
                if(!countries.contains(c)){
                    countries.add(c);
                }
                if(countries.size() >= 10){
                    break;
                }
            }
        }

        if(countries.size() < 10){
            for( Country c : countryRepository.findAll() ){
                if(!countries.contains(c)){
                    countries.add(c);
                }
                if(countries.size() >= 10){
                    break;
                }
            }
        }

        return countries;
    }

    public Page<Country> findCountryPage(int pageNumber, String countryName) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        List<Country> countries = null;

        if(user == null){
            countries = countryRepository.findCountryPage( "%" + countryName + "%", 0, PageRequest.of(pageNumber,12) );
        }else{
            countries = countryRepository.findCountryPage( "%" + countryName + "%", user.getId(), PageRequest.of(pageNumber,12) );
        }

        return new PageImpl<Country>(countries, PageRequest.of(pageNumber,12), countryRepository.countFindCountryPage("%" + countryName + "%"));
    }



}

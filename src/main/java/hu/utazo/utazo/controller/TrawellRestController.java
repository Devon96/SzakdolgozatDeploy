package hu.utazo.utazo.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.City;
import hu.utazo.utazo.model.Country;
import hu.utazo.utazo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TrawellRestController {



    CityService cityService;
    CountryService countryService;
    RatingService ratingService;
    AttractionService attractionService;
    UserService userService;

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    class InnerClass {
        String countryid;
        long id;
        boolean orszag;
        String countryname;
        String name;

    }

    @Autowired
    public TrawellRestController(CityService cityService, CountryService countryService, RatingService ratingService, AttractionService attractionService, UserService userService) {
        this.cityService = cityService;
        this.countryService = countryService;
        this.ratingService = ratingService;
        this.attractionService = attractionService;
        this.userService = userService;
    }

    @GetMapping("/add-rating/{attractionId}/{rating}")
    public boolean addRating(@PathVariable long attractionId, @PathVariable int rating){
        return ratingService.save(attractionId, rating);
    }

    @GetMapping("/get-cities-by-country-id/{id}")
    public ArrayList<City> getCitiesByCountryID(@PathVariable String id){

        return cityService.findAllByCountry(id);
    }

    @GetMapping("/get-ratings/{attractionId}")
    public List<int[]> getRatingCountsByAttractionId(@PathVariable long attractionId){
        return ratingService.getRatingCountsByAttractionId(attractionId);
    }

    @GetMapping("/get-cities-and-countries-by-name/{name}")
    public ArrayList<InnerClass> getCitiesAndCountriesByName(@PathVariable String name){

        ArrayList<City> cities = cityService.findFirst5ByNameLike(name);
        ArrayList<Country> countries = countryService.findFirst5ByNameLike(name);

        ArrayList<InnerClass> out = new ArrayList<>();

        for (int i = 0; i < countries.size() && out.size() <= 5; i++){
            InnerClass innerClass = new InnerClass();
            innerClass.countryid = countries.get(i).getId();
            innerClass.orszag = true;
            innerClass.name = countries.get(i).getName();
            out.add(innerClass);
        }
        for (int i = 0; i < cities.size() && out.size() <= 5; i++){
            InnerClass innerClass = new InnerClass();
            innerClass.id = cities.get(i).getId();
            innerClass.countryname = cities.get(i).getCountry().getName();
            innerClass.orszag = false;
            innerClass.name = cities.get(i).getName();
            out.add(innerClass);
        }
        return out;
    }

    @GetMapping("/get-saved-attractions-for-user")
    public List<Attraction> getSavedAttractionsByName(){
        return attractionService.getAttractionsOrderedByCountry();
    }

    @GetMapping("/remove-attraction-for-user/{id}")
    public boolean removeAttractionForUser(@PathVariable long id){
        userService.removeToUserAttractions(id);
        return true;
    }



}

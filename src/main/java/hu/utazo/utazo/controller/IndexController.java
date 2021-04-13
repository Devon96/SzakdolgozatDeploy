package hu.utazo.utazo.controller;


import hu.utazo.utazo.model.City;
import hu.utazo.utazo.model.Country;
import hu.utazo.utazo.service.AttractionService;
import hu.utazo.utazo.service.CityService;
import hu.utazo.utazo.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    CountryService countryService;
    CityService cityService;
    AttractionService attractionService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public IndexController(CountryService countryService, CityService cityService, AttractionService attractionService) {
        this.countryService = countryService;
        this.cityService = cityService;
        this.attractionService = attractionService;
    }

    @GetMapping("/")
    public String indexPage(Model model){
        model.addAttribute("title", "TraWell");
        model.addAttribute("bestCountries", countryService.findProposalCountries());
        model.addAttribute("bestCities", cityService.findPopularCities());
        model.addAttribute("suggestCities", cityService.findTopCitiesForMe());

        return "index";
    }

    @GetMapping("/country/{countriId}")
    public String countryPage(Model model, @PathVariable String countriId) throws Exception {
        Country country = countryService.findById(countriId);
        model.addAttribute("country", country);
        model.addAttribute("title", country.getName());
        return "country";
    }

    @GetMapping("/city/{cityId}")
    public String cityPage(Model model, @PathVariable long cityId){
        City city = cityService.findById(cityId);
        model.addAttribute("title", city.getName());
        model.addAttribute("city", city);

        return "city";
    }

    @GetMapping("/city")
    public String cityPageByCountryAndCityName(@RequestParam("countryName") String countryName, @RequestParam("cityName") String cityName) throws Exception {
        long cityId = cityService.findCytyIdByNameAndCountryName(cityName, countryName);

        return "redirect:/city/" + cityId;
    }


}

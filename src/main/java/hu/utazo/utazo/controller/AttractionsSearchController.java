package hu.utazo.utazo.controller;

import hu.utazo.utazo.model.City;
import hu.utazo.utazo.model.Country;
import hu.utazo.utazo.service.AttractionService;
import hu.utazo.utazo.service.CityService;
import hu.utazo.utazo.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/list")
public class AttractionsSearchController {

    CountryService countryService;
    CityService cityService;
    AttractionService attractionService;

    @Autowired
    public AttractionsSearchController(CountryService countryService, CityService cityService, AttractionService attractionService) {
        this.countryService = countryService;
        this.cityService = cityService;
        this.attractionService = attractionService;
    }

    @GetMapping("/countries/{pageNumber}")
    public String findInCountry( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name ){
        model.addAttribute("title", "Országok");
        model.addAttribute("page", countryService.findCountryPage(pageNumber, name) );
        model.addAttribute("site", "countries");
        return "all";
    }

    @GetMapping("/cities/all/{pageNumber}")
    public String findAllCity( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name ){
        model.addAttribute("title", "Városok");
        model.addAttribute("page", cityService.allCitiesPage(pageNumber, name));
        model.addAttribute("site", "cities/all");
        return "all";
    }

    @GetMapping("/cities/{countryId}/{pageNumber}")
    public String findAllCitiesInCountry( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name, @PathVariable String countryId ) throws Exception {
        Country c = countryService.findById(countryId);
        model.addAttribute("title", c.getName() + " - népszerű városok");
        model.addAttribute("page", cityService.findAllCitiesInCountry(name, countryId, pageNumber));
        model.addAttribute("site", "cities/" + c.getId());
        return "all";
    }


    @GetMapping("/attractions/{countryId}/{pageNumber}")
    public String findAllAttractionsInCountry( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name, @PathVariable String countryId ) throws Exception {
        Country c = countryService.findById(countryId);
        model.addAttribute("title", c.getName() + " - népszerű látnivalók");
        model.addAttribute("page", attractionService.findAllAttractionsInCountry(name, countryId, pageNumber));
        model.addAttribute("site", "attractions/" + c.getId());
        return "all";
    }

    @GetMapping("/attractions/proposal/{countryId}/{pageNumber}")
    public String findAllProposalAttractionsInCountry( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name, @PathVariable String countryId ) throws Exception {
        Country c = countryService.findById(countryId);
        model.addAttribute("title", c.getName() + " - javasolt látnivalók");
        model.addAttribute("page", attractionService.findAllProposalAttractionsInCountry(name, countryId, pageNumber));
        model.addAttribute("site", "attractions/proposal/" + c.getId());
        return "all";
    }

    @GetMapping("/attractions/city/{cityId}/{pageNumber}")
    public String findAllTopAttractionsInCity( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name, @PathVariable long cityId ) throws Exception {
        City c = cityService.findById(cityId);
        model.addAttribute("title", c.getName() + " - legnépszerűbb látnivalók");
        model.addAttribute("page", attractionService.findAllAttractionsInCity(name, cityId, pageNumber));
        model.addAttribute("site", "attractions/city/" + c.getId());
        return "all";
    }

    @GetMapping("/attractions/citysuggestion/{cityId}/{pageNumber}")
    public String findAllProposalAttractionsInCity( Model model, @PathVariable int pageNumber, @RequestParam(defaultValue = "") String name, @PathVariable long cityId ) throws Exception {
        City c = cityService.findById(cityId);
        model.addAttribute("title", c.getName() + " - javasolt látnivalók");
        model.addAttribute("page", attractionService.findAllSuggestionAttractionsInCity(name, cityId, pageNumber));
        model.addAttribute("site", "attractions/citysuggestion/" + c.getId());
        return "all";
    }

}

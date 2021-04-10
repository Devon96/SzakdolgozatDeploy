package hu.utazo.utazo.controller;


import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.City;
import hu.utazo.utazo.model.Country;
import hu.utazo.utazo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class UploadController {

    CountryService countryService;
    ContinentService continentService;
    CityService cityService;
    AttractionService attractionService;
    LabelService labelService;
    TypeService typeService;

    @Autowired
    public UploadController(CountryService countryService, ContinentService continentService, CityService cityService, AttractionService attractionService, LabelService labelService, TypeService typeService) {
        this.countryService = countryService;
        this.continentService = continentService;
        this.cityService = cityService;
        this.attractionService = attractionService;
        this.labelService = labelService;
        this.typeService = typeService;
    }

    @GetMapping("/country")
    public String countryUpload(Country country, Model model){
        model.addAttribute("continents", continentService.findAll());
        model.addAttribute("title", "Ország feltöltés");
        return "uploads/upload_country";
    }

    @PostMapping("/country")
    public String countryUploadPost(@RequestParam("countryImage") MultipartFile countryImage, Country country) throws IOException {
        countryService.save(country, countryImage);
        return "redirect:/";
    }

    @GetMapping("/city")
    public String cityUpload(City city, Model model){
        model.addAttribute("continents", continentService.findAll());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("title", "Város feltöltés");
        return "uploads/upload_city";
    }

    @PostMapping("/city")
    public String cityUploadPost(@RequestParam("cityImage") MultipartFile cityImage, City city) throws IOException {
        cityService.save(city, cityImage);
        return "redirect:/";
    }

    @GetMapping("/attraction")
    public String attractionUpload(Attraction attraction, Model model){
        model.addAttribute("title", "Látnivaló feltöltés");
        model.addAttribute("continents", continentService.findAll());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("labels", labelService.fingAll());
        model.addAttribute("types", typeService.findAll());

        //model.addAttribute("cities", cityService.findAll());
        return "uploads/upload_attraction";
    }

    @PostMapping("/attraction")
    public String attractionUploadPost(@RequestParam("attractionImage") MultipartFile[] attractionImage, Attraction attraction) throws Exception {

        attractionService.save(attraction, attractionImage);
        return "redirect:/";
    }
}

package hu.utazo.utazo.controller;

import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.Comment;
import hu.utazo.utazo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
@RequestMapping("/attraction")
public class AttractionController {

    AttractionService attractionService;
    UserService userService;
    CommentService commentService;
    RatingService ratingService;
    CountryService countryService;
    LabelService labelService;
    TypeService typeService;

    @Autowired
    public AttractionController(AttractionService attractionService, UserService userService, CommentService commentService, RatingService ratingService, CountryService countryService, LabelService labelService, TypeService typeService) {
        this.attractionService = attractionService;
        this.userService = userService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        this.countryService = countryService;
        this.labelService = labelService;
        this.typeService = typeService;
    }

    @GetMapping("/{attractionId}")
    public String attractionPage(Model model, @PathVariable long attractionId, Comment comment) throws Exception {

        Attraction attraction = attractionService.findById(attractionId);

        Path path = Paths.get("src/main/resources/static/photos/" + attraction.getCity().getCountry().getId() + "/" + attraction.getCity().getId() + "/" + attraction.getId());
        File f = new File(path.toString());
        model.addAttribute("imagelist",f.list());

        model.addAttribute("title", attraction.getName());
        model.addAttribute("attraction", attraction);
        model.addAttribute("rating", ratingService.findByAttractionAndUser(attractionId));

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            model.addAttribute("attractionSaved", userService.find().getAttractions().stream().filter(i -> i.getAttraction().getId() == attractionId).toArray().length == 0);
        }else{
            model.addAttribute("attractionSaved", false);

        }


        return "attraction";
    }


    @GetMapping("/add-to-user/{attractionId}")
    public String addAtraction(@PathVariable long attractionId){
        userService.addToUserAttractions(attractionId);
        return "redirect:/attraction/" + attractionId;
    }

    @GetMapping("/remove-from-user/{attractionId}")
    public String removeAtraction(@PathVariable long attractionId){
        userService.removeToUserAttractions(attractionId);
        return "redirect:/attraction/" + attractionId;
    }

    @PostMapping("/comment/add/{attractionId}")
    public String addComment(@PathVariable long attractionId, Comment comment) throws Exception {

        commentService.save(comment, attractionId);

        return "redirect:/attraction/" + attractionId;
    }

    @GetMapping("/comment/delete/{attractionId}/{commentId}")
    public String deleteComment(@PathVariable long attractionId ,@PathVariable long commentId) throws Exception {
        commentService.delete(commentId);
        return "redirect:/attraction/" + attractionId;
    }

    @GetMapping("/delete/{attractionId}")
    public String deleteAttraction(@PathVariable long attractionId) throws Exception {
        Attraction attraction = attractionService.delete(attractionId);
        return "redirect:/city/" + attraction.getCity().getId();
    }

    @GetMapping("/saved")
    public String savedAttractions(Model model) throws Exception {
        model.addAttribute("title", "Mentett látnivalók");
        return "saved-attractions";
    }

    @GetMapping("/edit")
    public String editAttractions(@RequestParam(defaultValue = "0") long attractionId, Model model ) throws Exception {
        Attraction attraction = attractionService.findById(attractionId);

        attraction.setLabelStrList(new ArrayList<>());
        attraction.getLabels().forEach(i -> {attraction.getLabelStrList().add(i.getId() + "");});

        model.addAttribute("attraction", attraction);
        model.addAttribute("title", attraction.getName());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("labels", labelService.fingAll());
        model.addAttribute("types", typeService.findAll());

        return "edit/edit_attraction";
    }

    @PostMapping("/edit")
    public String attractionUploadPost(Attraction attraction) throws Exception {
        attractionService.edit(attraction);
        return "redirect:/attraction/" + attraction.getId();
    }

}

package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.Rating;
import hu.utazo.utazo.model.User;
import hu.utazo.utazo.repository.AttractionRepository;
import hu.utazo.utazo.repository.RatingRepository;
import hu.utazo.utazo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    UserRepository userRepository;
    AttractionRepository attractionRepository;
    RatingRepository ratingRepository;

    @Autowired
    public RatingService(UserRepository userRepository, AttractionRepository attractionRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
        this.ratingRepository = ratingRepository;
    }

    public boolean save(long attractionId, int rating){
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);

        if(user == null || attraction == null || rating < 1 || rating > 5){
            return false;
        }

        Rating ratingObject = ratingRepository.findByAttractionAndUser(attraction, user).orElse(null);


        if(ratingObject == null){
            ratingObject = new Rating();
            ratingObject.setAttraction(attraction);
            ratingObject.setUser(user);
            ratingObject.setRating(rating);
            ratingRepository.save(ratingObject);
            return true;
        }
        ratingObject.setRating(rating);
        ratingRepository.save(ratingObject);
        return true;

    }

    public Rating findByAttractionAndUser(long attractionId) throws Exception {
        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);
        if(attraction == null){
            throw new Exception("Nincs ilyen látnivaló");
        }
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(user == null){
            return null;
        }

        return ratingRepository.findByAttractionAndUser(attraction, user).orElse(null);

    }

    public List<int[]> getRatingCountsByAttractionId(long attractionId){
        return ratingRepository.getRatingsToTemplate2(attractionId);
    }
}

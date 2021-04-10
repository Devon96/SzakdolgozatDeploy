package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Attraction;
import hu.utazo.utazo.model.Authorities;
import hu.utazo.utazo.model.Comment;
import hu.utazo.utazo.model.User;
import hu.utazo.utazo.repository.AttractionRepository;
import hu.utazo.utazo.repository.CommentRepository;
import hu.utazo.utazo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.ContentHandler;
import java.util.Date;

@Service
public class CommentService {

    CommentRepository commentRepository;
    UserRepository userRepository;
    AttractionRepository attractionRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, AttractionRepository attractionRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
    }



    public void save(Comment comment, long attractionId) throws Exception {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);
        if (user == null){
            throw new Exception("Nincs ilyen felhasználó!");
        }
        if (attraction == null){
            throw new Exception("Nincs ilyen látnivaló!");
        }
        comment.setAttraction(attraction);
        comment.setUser(user);
        comment.setDatetime(new Date());

        commentRepository.save(comment);

    }

    public void delete(long commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(comment == null){
            throw new Exception("Nincs ilyen komment");
        }
        if(user == null){
            throw new Exception("Nincs ilyen felhasználó");
        }

        boolean admin = false;
        boolean iro = comment.getUser().getUsername().equals(user.getUsername());

        for(Authorities a : user.getRoles()){
            if (a.getName().equals("ADMIN")) {
                admin = true;
                break;
            }
        }

        if(!admin && !iro){
            throw new Exception("Nincs jogod kitörölni ezt kommentet!");
        }

        commentRepository.delete(comment);
    }
}

package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SavedAttraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.EAGER)
    Attraction attraction;

    @ManyToOne
    User user;

    @Override
    public String toString(){
        return "useremail: " + this.user.getEmail() + " attractionId: " + this.attraction.getId();
    }
}

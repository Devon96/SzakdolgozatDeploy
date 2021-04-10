package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    User user;

    @ManyToOne
    Attraction attraction;

    int rating;

    @Transient
    Integer count;

}

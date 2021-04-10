package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @Lob
    String text;

    Date datetime;

    @ManyToOne
    Attraction attraction;

    @ManyToOne
    User user;


}

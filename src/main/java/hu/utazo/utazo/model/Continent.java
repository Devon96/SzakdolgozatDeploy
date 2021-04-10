package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Continent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    //@OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "continent", orphanRemoval = true )
    //List<Country> countries;


}

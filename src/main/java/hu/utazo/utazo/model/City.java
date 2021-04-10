package hu.utazo.utazo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Size(min = 2, message = "A város nevének minimum 3 karakternek kell lennie.")
    String name;

    @Lob
    String description;

    //@JsonIgnore
    @ManyToOne
    Country country;

    //Ha ezt megadom gond lessz az apinál mert egymásra hivatkoznak az objektumok
    @JsonIgnore
    @OneToMany( orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "city", cascade = CascadeType.ALL )
    List<Attraction> attractions;

    @Transient
    List<Attraction> BestAttractions;

    @Transient
    List<Attraction> SuggestAttractions;

    @Override
    public String toString(){
        return "id: " + this.getId() + " name: " + this.getName();
    }


}

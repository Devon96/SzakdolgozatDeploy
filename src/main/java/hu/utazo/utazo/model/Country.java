package hu.utazo.utazo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Country {

    @Id
    String id;

    @NotNull
    @Size(min = 3, message = "A névnek minimum 3 karakternek kell lennie.")
    String name;

    @NotNull
    @Size(min = 3, message = "A leírásnak minimum 3 karakternek kell lennie.")
    @Lob
    String description;

    @JsonIgnore
    @ManyToOne
    Continent continent;

    //Ha ezt megadom gond lessz az apinál mert egymásra hivatkoznak az objektumok
    @JsonIgnore
    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "country", orphanRemoval = true )
    List<City> allCity;

    @Transient
    List<City> cities;

    @Transient
    List<Attraction> topAttractions;

    @Transient
    List<Attraction> SuggestAttractions;

    @Override
    public String toString(){
        return "id: " + this.getId() + ", name: " + this.getName();
    }
}

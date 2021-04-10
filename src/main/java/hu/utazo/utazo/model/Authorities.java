package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String name;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "roles")
    private List<User> users = new ArrayList<>();

}

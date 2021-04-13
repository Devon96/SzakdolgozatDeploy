package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @Size(min = 3, message = "A a cimke nevének minimum 3 karakternek kell lennie.")
    String name;

    @ManyToMany( cascade = CascadeType.ALL, mappedBy = "labels", fetch = FetchType.LAZY)
    List<Attraction> attractions;


    @Override
    public int hashCode(){
        return (int) id;
    }

    @Override
    public String toString(){
        return "id: " + this.id + "  :  " + this.name;
    }

    @PreUpdate
    public void preUpdate(){
        this.name = this.name.toLowerCase();
    }
    @PrePersist
    public void prePersist(){
        this.name = this.name.toLowerCase();
    }

}

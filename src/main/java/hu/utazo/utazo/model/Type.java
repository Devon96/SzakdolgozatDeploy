package hu.utazo.utazo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class Type {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    @Size(min = 2, message = "A típusnévnek minimum két karakternek kell lennie.")
    String name;

    @Transient
    String newType;

    @Override
    public String toString(){
        return "id: " + this.id + " name: " + this.name;
    }

    @PrePersist
    @PreUpdate
    public void preUpdate(){
        this.name = this.name.toLowerCase();
    }

}

package hu.utazo.utazo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull
    @ManyToOne
    Type type;

    @NotNull
    @Size( min = 3, message = "A névnek minimum 3 karakternek kell lennie." )
    String name;

    @NotNull
    @Size( min = 8, message = "A leírásnak minimum 8 karakternek kell lennie." )
    @Lob
    String description;

    @NotNull
    int visitTime;

    String address;

    String website;

    //@JsonIgnore
    @ManyToOne
    City city;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "attraction_labels",
            joinColumns = {@JoinColumn(name="attraction_id")},
            inverseJoinColumns = {@JoinColumn(name="label_id")}
    )
    List<Label> labels;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "attraction")
    Collection<SavedAttraction> savedAttractions;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "attraction", fetch = FetchType.LAZY )
    List<Rating> ratings;

    @Transient
    List<String> labelStrList;

    @Transient
    List<Attraction> similarAttractions;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "attraction" )
    List<Comment> comments;

    @Override
    public int hashCode(){
        return (int) this.getId();
    }

    @Override
    public String toString(){
        return "id: " + this.getId() + " name: " + this.getName();
    }
    

    @LastModifiedBy
    private String lastModifiedBy;


    @LastModifiedDate
    private Date lastModifiedDate;

}

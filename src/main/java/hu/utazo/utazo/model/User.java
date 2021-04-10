package hu.utazo.utazo.model;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Map;

@Entity
@Data
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Size(min = 3, message = "A felhasználónévnek minimum 3 karakternek kell lennie.")
    @Column( unique=true )
    String username;

    @Size( min = 3, message = "A jelszónak minimum 3 karakternek kell lennie." )
    String password;

    @Email
    @Column(unique=true)
    String email;

    @NotNull
    boolean enabled;

    String verificationCode;

    String googleId;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name="users_username")},
            inverseJoinColumns = {@JoinColumn(name="roles_id")}
    )
    Collection<Authorities> roles;


    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    Collection<SavedAttraction> attractions;



    public User(){}

    public User(Map<String, Object> attrMap){

        this.setEmail((String)attrMap.get("email"));
        this.setUsername((String)attrMap.get("name"));
        this.setEnabled(true);
    }

    public boolean isAdmin(){
        return this.roles.stream().anyMatch(i -> i.getName().equals("ADMIN"));
    }
}

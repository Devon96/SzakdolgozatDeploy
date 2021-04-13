package hu.utazo.utazo.config;

import hu.utazo.utazo.model.Authorities;
import hu.utazo.utazo.model.Continent;
import hu.utazo.utazo.model.Type;
import hu.utazo.utazo.model.User;
import hu.utazo.utazo.repository.ContinentRepository;
import hu.utazo.utazo.repository.TypeRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;
import hu.utazo.utazo.repository.AuthoritiesRepository;
import hu.utazo.utazo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDataSetup implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private AuthoritiesRepository authoritiesRepository;
    private PasswordEncoder passwordEncoder;
    private ContinentRepository continentRepository;

    boolean alredySetup = false;

    @Autowired
    public UserDataSetup(UserRepository userRepository, AuthoritiesRepository authoritiesRepository, PasswordEncoder passwordEncoder, ContinentRepository continentRepository) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.passwordEncoder = passwordEncoder;
        this.continentRepository = continentRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(alredySetup){
            return;
        }

        createRoleIfNotExist("ADMIN");
        createRoleIfNotExist("USER");

        Authorities role = authoritiesRepository.findByName("ADMIN");
        Authorities role2 = authoritiesRepository.findByName("USER");



        User user = new User();
        user.setEnabled(true);
        user.setUsername("user");
        user.setEmail("alma@gmail.com");
        user.setRoles(Arrays.asList(role, role2));
        user.setPassword(passwordEncoder.encode("a"));
        createUserIfNotExist(user);


        createContinentIfNotExist("Európa");
        createContinentIfNotExist("Ázsia");
        createContinentIfNotExist("Amerika");
        createContinentIfNotExist("Afrika");
        createContinentIfNotExist("Ausztrália");


        alredySetup = true;
    }
    

    @Transactional
    public Authorities createRoleIfNotExist(String name) {
        Authorities role = authoritiesRepository.findByName(name);

        if(role == null){
            role = new Authorities();
            role.setName(name);
            authoritiesRepository.save(role);
        }
        return role;
    }

    @Transactional
    public void createContinentIfNotExist(String name) {

        Continent continent = continentRepository.findByName(name);

        if(continent == null){
            continent = new Continent();
            continent.setName(name);
            continentRepository.save(continent);
        }
    }

    @Transactional
    public void createUserIfNotExist(User user) {

        User u = userRepository.findByUsername(user.getEmail()).orElse(null);

        if(u == null){
            userRepository.save(user);
        }
    }


}

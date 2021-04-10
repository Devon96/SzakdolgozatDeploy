package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Authorities;
import hu.utazo.utazo.model.User;
import hu.utazo.utazo.repository.AuthoritiesRepository;
import hu.utazo.utazo.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;

@Service
public class MyOidcUserService extends OidcUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthoritiesRepository authoritiesRepository;


    @Autowired
    public MyOidcUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthoritiesRepository authoritiesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authoritiesRepository = authoritiesRepository;
    }

    @SneakyThrows
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws InternalAuthenticationServiceException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) throws Exception {
        /*User newUser = new User(oidcUser.getAttributes());
        User user = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        if(user == null){
            user = userRepository.findByEmail(oidcUser.getName()).orElse(null);
        }
        if (user == null) {
            user = new User();
            user.setEmail(newUser.getEmail());
            user.setUsername(newUser.getUsername());
            user.setEnabled(true);

            String alphabet = "AÁBCDEÉFGHIÍJKLMNOÖŐPQRSTUÚÜŰVWXYZaábcdeéfghiíjklmnoóöő0pqrstuúüűvwxyz0123456789 _.-";
            Random random = new Random();
            StringBuilder pwd = new StringBuilder();
            for(int i = 0; i < 20; i++){
                pwd.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            user.setPassword(passwordEncoder.encode(pwd));

            Authorities userRole = authoritiesRepository.findByName("USER");
            user.setRoles(Collections.singletonList(userRole));
            user.setGoogleId(oidcUser.getName());
            user = userRepository.save(user);
        }
        if(user.getGoogleId() == null || ! user.getGoogleId().equals(oidcUser.getName())){
            user.setGoogleId(oidcUser.getName());
            userRepository.save(user);
        }
        if(!user.isEnabled()){
            throw new Exception("Ez a felhasználó le van tiltva");
        }

*/
        return oidcUser;
    }


}

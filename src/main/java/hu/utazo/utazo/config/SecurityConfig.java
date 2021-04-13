package hu.utazo.utazo.config;


import hu.utazo.utazo.model.Authorities;
import hu.utazo.utazo.model.User;
import hu.utazo.utazo.repository.UserRepository;
import hu.utazo.utazo.service.MyOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyOidcUserService myOidcUserService;

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .authoritiesByUsernameQuery("SELECT Email, Name FROM AUTHORITIES, USERS, USERS_ROLES WHERE USERS_ROLES.USERS_USERNAME = USERS.ID AND AUTHORITIES.ID = USERS_ROLES.ROLES_ID AND USERS.Email = ?")
                .usersByUsernameQuery("SELECT Email, Password, Enabled FROM USERS WHERE USERS.Email = ?")
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/attraction/comment/delete/**/**",
                        "/attraction/delete/**",
                        "/attraction/edit",
                        "/users/list",
                        "/users/list/**",
                        "/users/ban/**",
                        "/users/admin-add-remove/**",
                        "/api/get-cities-by-country-id/**",
                        "/upload/country",
                        "/upload/city",
                        "/upload/attraction"
                ).hasAuthority("ADMIN")

                .antMatchers(
                        "/attraction/add-to-user/**",
                        "/attraction/remove-from-user/**",
                        "/attraction/comment/add/**",
                        "/attraction/comment/delete/**/**",
                        "/attraction/saved",
                        "/list/saved",
                        "/list/attractions/proposal/**/**",
                        "/list/attractions/citysuggestion/**/**",
                        "/user/edit",
                        "/api/add-rating/**/**",
                        "/api/get-saved-attractions-for-user/**",
                        "/api/remove-attraction-for-user/**"
                ).hasAuthority("USER")

                .antMatchers(
                        "/css/**",
                        "/script/**",
                        "/images/**",
                        "/bootstrap/**",
                        "/jquery/**",
                        "/**",
                        "/semantic-ui/**",
                        "/semantic-ui/**/**",
                        "/semantic-ui/**/**/**",
                        "/semantic-ui/**/**/**/**",
                        "/semantic-ui/**/**/**/**/**",
                        "/semantic-ui/semantic.css",
                        "/attraction/**",
                        "/list/countries/**",
                        "/list/cities/all/**",
                        "/list/cities/**/**",
                        "/list/attractions/**/**",
                        "/list/attractions/city/**/**",
                        "/login",
                        "/register",
                        "/register/validation/**",
                        "/error",
                        "/",
                        "/country/**",
                        "/city/**",
                        "/city",
                        "/api/get-ratings/**",
                        "/api/get-cities-and-countries-by-name/**").permitAll()
                .and()
                .logout().permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .rememberMe().key("ezazentitkosKoDom42420")
                .and()
                .oauth2Login()
                    .loginPage("/oauth_login")
                        .userInfoEndpoint()
                            .oidcUserService(myOidcUserService).userAuthoritiesMapper(this.userAuthoritiesMapper());
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;

                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    User user =  userRepository.findByEmail(idToken.getEmail()).orElse(null);

                    if(user != null){
                        for (Authorities a : user.getRoles()){
                            mappedAuthorities.add(new GrantedAuthority() {
                                @Override
                                public String getAuthority() {
                                    return a.getName();
                                }
                            });
                        }
                    }
                }
            });

            return mappedAuthorities;
        };
    }


}
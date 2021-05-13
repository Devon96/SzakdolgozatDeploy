package hu.utazo.utazo.service;

import hu.utazo.utazo.model.*;
import hu.utazo.utazo.repository.AttractionRepository;
import hu.utazo.utazo.repository.AuthoritiesRepository;
import hu.utazo.utazo.repository.SavedAttractionRepository;
import hu.utazo.utazo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Random;

@Service
public class UserService {

    UserRepository userRepository;
    AuthoritiesRepository authoritiesRepository;
    PasswordEncoder passwordEncoder;
    JavaMailSender javaMailSender;
    AttractionRepository attractionRepository;
    SavedAttractionRepository savedAttractionRepository;

    @Value("${spring.mail.username}")
    private String EMAIL_ADDRESS;

    @Value("${server.port}")
    private int PORT;

    @Value("${serverurl}")
    private String URL;

    @Autowired
    public UserService(UserRepository userRepository, AuthoritiesRepository authoritiesRepository, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, AttractionRepository attractionRepository, SavedAttractionRepository savedAttractionRepository) {
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.attractionRepository = attractionRepository;
        this.savedAttractionRepository = savedAttractionRepository;
    }

    public void save(User user) throws Exception {

        Authorities authorities = authoritiesRepository.findByName("USER");
        user.setRoles(Collections.singletonList(authorities));
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));



        User user1 = userRepository.findByEmail(user.getEmail()).orElse(null);

        if(user1 == null){
            sendMail(user);
            userRepository.save(user);
        }else{
            throw new Exception("Ezzel az email címmel már regisztráltak.");
        }
    }

    public void sendMail(User user) throws MessagingException {

        MimeMessage mimeMessage = null;

        try {
            mimeMessage = this.javaMailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress(EMAIL_ADDRESS));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));


            Random random = new Random();
            String verification_code = random.ints('a', 'z' + 1).limit(20)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            user.setVerificationCode(verification_code);

            String body = "<h2>Kedves " + user.getUsername() + "!</h2><br/><br/>A regisztrációd megerősítéséhez kattints <a href='" + URL + "register/validation/" + verification_code + "'>ERRE A LINKRE</a>";
            mimeMessage.setSubject("TraWell regisztráció");
            mimeMessage.setText(body, "UTF-8", "html");
            javaMailSender.send(mimeMessage);


        }catch (MessagingException e){
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public void validate(String code) {
        User user = userRepository.findByVerificationCode(code);
        if(user != null){
            user.setEnabled(true);
            user.setVerificationCode("");
            userRepository.save(user);
        }
    }

    public void addToUserAttractions(long attractionId) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);

        if(user != null && attraction != null){

            for(SavedAttraction sa : user.getAttractions()){
                if (sa.getAttraction().getId() == attractionId){
                    return;
                }
            }

            SavedAttraction savedAttraction = new SavedAttraction();
            savedAttraction.setAttraction(attraction);
            savedAttraction.setUser(user);

            user.getAttractions().add(savedAttraction);
            userRepository.save(user);

        }
    }

    public User find() throws Exception {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if(user == null){
            throw new Exception("A felhasználó nincs bejelentkezve.");
        }

        return user;
    }

    @Transactional
    public void removeToUserAttractions(long attractionId) {

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        Attraction attraction = new Attraction();
        attraction.setId(attractionId);

        if(user != null){
            savedAttractionRepository.deleteByAttractionAndUser(attraction, user);
        }
    }

    public Page<User> findAll(String search, int pageNumber) {
        return userRepository.findAllWithFilter("%" + search + "%", PageRequest.of(pageNumber,10));
    }

    public void negateEnabled(long userId) throws Exception {
        User u = userRepository.findById(userId).orElse(null);
        if(u == null){
            throw new Exception("Nincs ilyen felhasználó!");
        }
        u.setEnabled( ! u.isEnabled() );
        userRepository.save(u);

    }

    public boolean updateUser(User user) throws Exception {
        User old = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(old == null){
            throw new Exception("Nincs bejelentkezve a felhasználó");
        }
        boolean returnBool = old.getEmail().equals(user.getEmail());
        old.setUsername(user.getUsername());
        old.setEmail(user.getEmail());

        if(! user.getPassword().equals("")){
            old.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if(! returnBool){
            old.setEnabled(false);
            sendMail(old);
        }
        userRepository.save(old);
        return ! returnBool;
    }

    public void addRemoveAdminRole(long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("Nincs ilyen felhasználó"));

        Authorities admin = authoritiesRepository.findByName("ADMIN");

        if(user.getRoles().contains(admin)){
            user.getRoles().remove(admin);
        }else {
            user.getRoles().add(admin);
        }
        userRepository.save(user);
    }

    public boolean isSaved(long id) {
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return true;
        }
        SavedAttraction sa = savedAttractionRepository.findFirstByUserIdAndAttraction(SecurityContextHolder.getContext().getAuthentication().getName(), id).orElse(null);
        return sa == null;
    }
}

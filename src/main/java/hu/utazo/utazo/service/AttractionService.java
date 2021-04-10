package hu.utazo.utazo.service;

import hu.utazo.utazo.model.*;
import hu.utazo.utazo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    AttractionRepository attractionRepository;
    CityRepository cityRepository;
    CountryRepository countryRepository;
    TypeRepository typeRepository;
    LabelRepository labelRepository;
    SavedAttractionRepository savedAttractionRepository;
    UserRepository userRepository;

    @Autowired
    public AttractionService(AttractionRepository attractionRepository, CityRepository cityRepository, CountryRepository countryRepository, TypeRepository typeRepository, LabelRepository labelRepository, SavedAttractionRepository savedAttractionRepository, UserRepository userRepository) {
        this.attractionRepository = attractionRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.typeRepository = typeRepository;
        this.labelRepository = labelRepository;
        this.savedAttractionRepository = savedAttractionRepository;
        this.userRepository = userRepository;
    }

    public void save(Attraction attraction, MultipartFile[] attractionImages) throws Exception {

        //ellenőrzöm hogy már az adatbázisban van e

        Attraction temp = attractionRepository.findByNameAndCity(attraction.getName(), attraction.getCity()).orElse(null);
        if(temp != null){
            throw new Exception("A hely már létezik");
        }

        //típus mentése
        if(attraction.getType().getId() == 0){
            Type t = typeRepository.findByNameIgnoreCase(attraction.getType().getName()).orElse(null);
            if(t == null){
                t = typeRepository.save(attraction.getType());
            }
            attraction.setType(t);
        }

        //label adatbázishoz adása ha az nem szerepel benne
        attraction.setLabels(new ArrayList<>());
        for(String str : attraction.getLabelStrList()){
            str = str.toLowerCase().strip();
            Label l = new Label();
            try {
                l.setId(Long.parseLong(str));
            }catch (Exception e){
                l.setId(0);
                if(str.length() < 3){
                    System.out.println("Túl rövid a cimka neve");
                    continue;
                }
                l.setName(str);
                l = labelRepository.save(l);
            }
            attraction.getLabels().add(l);
        }


        attractionRepository.save(attraction);
        attraction = attractionRepository.findFirstByOrderByIdDesc();
        attraction.getCity().setCountry(cityRepository.findCountryIdByCityId(attraction.getCity().getId()));


        City c = attraction.getCity();
        Country co = attraction.getCity().getCountry();


        Path directoriesPath = Paths.get("src/main/resources/static/photos/" + attraction.getCity().getCountry().getId() + "/" + attraction.getCity().getId() + "/" + attraction.getId());
        Files.createDirectories(directoriesPath);

        for(int i = 0; i < attractionImages.length; i++){
            byte[] imageBytes = attractionImages[i].getBytes();
            Path filePath = Paths.get("src/main/resources/static/photos/" + attraction.getCity().getCountry().getId() + "/" + attraction.getCity().getId() + "/" + attraction.getId() + "/" + i + ".jpg");
            try{
                Files.createFile(filePath);
                Files.write(filePath, imageBytes);
            }catch (FileAlreadyExistsException e){
                System.out.println("A kép már létezik");
            }
        }

    }

    public Attraction findById(long attractionId) throws Exception {
        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);
        if(attraction == null){
            throw new Exception("Nincs ilyen látnivaló");
        }
        Set<Label> attractionLabels = new HashSet<>(attraction.getLabels());

        Map<Attraction, Double> attractionMap = new HashMap<>();

        List<Attraction> attractionsInTheCountry = attractionRepository.findBestsByCountry(attraction.getCity().getCountry().getId(), PageRequest.of(0, 100));

        for (Attraction a : attractionsInTheCountry){
            int osszeg = (attractionLabels.size() + a.getLabels().size());
            Set<Label> tempset = new HashSet<>(attractionLabels);
            tempset.addAll(Set.copyOf(a.getLabels()));

            int egyesitettOsszeg = tempset.size();

            attractionMap.put(a, osszeg / (double) egyesitettOsszeg);

        }

        attraction.setSimilarAttractions(new ArrayList<>());

        for(int i = 0; i < 10 && attractionMap.size() > 1; i++){

            Attraction a = null;
            double max = Double.MIN_VALUE;
            for(Map.Entry<Attraction, Double> entry : attractionMap.entrySet()){
                if(entry.getValue() > max && entry.getKey().getId() != attractionId){
                    a = entry.getKey();
                    max = entry.getValue();
                }
            }

            attraction.getSimilarAttractions().add(a);
            attractionMap.remove(a);
        }




        return attraction;
    }

    public Attraction delete(long attractionId) throws Exception{

        Attraction attraction = attractionRepository.findById(attractionId).orElse(null);

        if(attraction == null){
            throw new Exception("Nincs ilyen látnivaló");
        }


        attractionRepository.delete(attraction);
        return attraction;

    }

    public List<Attraction> getAttractionsOrderedByCountry(){
        return savedAttractionRepository.getAttractionsOrderedByCountry(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void edit(Attraction attraction) throws Exception {
        //Type
        if(attraction.getType().getId() == 0){
            attraction.getType().setName(attraction.getType().getNewType().toLowerCase());
            Type t = typeRepository.findByNameIgnoreCase(attraction.getType().getName()).orElse(null);
            if(t == null){
                t = new Type();
                t.setName(attraction.getType().getName());
                t = typeRepository.save(t);
            }
            attraction.setType(t);
        }

        //label adatbázishoz adása ha az nem szerepel benne
        attraction.setLabels(new ArrayList<>());
        for(String str : attraction.getLabelStrList()){
            str = str.toLowerCase().strip();
            Label l = new Label();
            try {
                l.setId(Long.parseLong(str));
            }catch (Exception e){
                l.setId(0);
                if(str.length() < 3){
                    System.out.println("Túl rövid a cimka neve");
                    continue;
                }
                l.setName(str);
                l = labelRepository.save(l);
            }
            attraction.getLabels().add(l);
        }

        //Ha változik a város a képek másolódnak
        Attraction oldAttraction = attractionRepository.findById(attraction.getId()).orElse(null);
        if(oldAttraction == null){
            throw new Exception("Nincs ilyen látnivaló.");
        }

        if(oldAttraction.getCity().getId() != attraction.getCity().getId()){
            Path fromPath = Paths.get("src/main/resources/static/photos/" + oldAttraction.getCity().getCountry().getId() + "/" + oldAttraction.getCity().getId() + "/" + attraction.getId());
            String countryId = countryRepository.getCountyIdByCityId(attraction.getCity().getId());
            Path toPath = Paths.get("src/main/resources/static/photos/" + countryId + "/" + attraction.getCity().getId() + "/" + attraction.getId());

            Files.move(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);

        }



        attractionRepository.save(attraction);
    }

    public Page<Attraction> findAllAttractionsInCountry(String name, String countryId, int pageNumber) {
        return attractionRepository.findAllAttractionsInCountry("%" + name + "%", countryId, PageRequest.of(pageNumber,12));
    }

    public Page<Attraction> findAllProposalAttractionsInCountry(String name, String countryId, int pageNumber) throws Exception {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(user == null){
            throw new Exception("A felhasználó nincs bejelentkezve.");
        }
        List<Attraction> attractions = attractionRepository.findAllProposalAttractionsInCountry("%" + name + "%", countryId, user.getId(), PageRequest.of(pageNumber,12));

        return new PageImpl<Attraction>(attractions, PageRequest.of(pageNumber,12), attractionRepository.countFindAllProposalAttractionsInCountry("%" + name + "%", countryId));

    }

    public Page<Attraction> findAllAttractionsInCity(String name, long cityId, int pageNumber) {
        return attractionRepository.findTopAttractionsInTheCity("%" + name + "%", cityId, PageRequest.of(pageNumber,12));
    }

    public Page<Attraction> findAllSuggestionAttractionsInCity(String name, long cityId, int pageNumber) throws Exception {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(user == null){
            throw new Exception("Nincs bejelentkezve a felhasználó");
        }
        List<Attraction> attractions = attractionRepository.findAllSuggestionAttractionsInCity("%" + name + "%", cityId, user.getId(), PageRequest.of(pageNumber,12));

        return new PageImpl<Attraction>(attractions, PageRequest.of(pageNumber,12), attractionRepository.countFindAllSuggestionAttractionsInCity("%" + name + "%", cityId));
    }
}

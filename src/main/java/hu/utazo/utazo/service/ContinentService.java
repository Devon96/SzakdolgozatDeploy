package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Continent;
import hu.utazo.utazo.repository.ContinentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContinentService {

    ContinentRepository continentRepository;

    @Autowired
    public ContinentService(ContinentRepository continentRepository) {
        this.continentRepository = continentRepository;
    }

    public List<Continent> findAll(){
        return continentRepository.findAll();
    }


}

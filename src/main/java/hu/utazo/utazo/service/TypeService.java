package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Type;
import hu.utazo.utazo.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public void add(Type type){
        typeRepository.save(type);
    }

    public List<Type> findAll(){
        return typeRepository.findAll();
    }
}

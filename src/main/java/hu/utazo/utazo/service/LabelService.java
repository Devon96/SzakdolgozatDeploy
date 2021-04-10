package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Label;
import hu.utazo.utazo.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    LabelRepository labelRepository;

    @Autowired
    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<Label> fingAll(){
        return labelRepository.findAll();
    }
}

package bsep.pkiapp.service;

import bsep.pkiapp.model.KeyUsage;
import bsep.pkiapp.repository.KeyUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeyUsageService {

    @Autowired
    private KeyUsageRepository keyUsageRepository;

    public KeyUsage getById(Integer id) {
        log.debug("GKU with ID: {}", id);
        return keyUsageRepository.getById(id);
    }

    public List<KeyUsage> getAll() {
        return keyUsageRepository.findAll();
    }
}

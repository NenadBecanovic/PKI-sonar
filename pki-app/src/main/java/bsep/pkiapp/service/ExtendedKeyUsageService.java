package bsep.pkiapp.service;

import bsep.pkiapp.model.ExtendedKeyUsage;
import bsep.pkiapp.repository.ExtendedKeyUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExtendedKeyUsageService {

    @Autowired
    private ExtendedKeyUsageRepository extendedKeyUsageRepository;

    public ExtendedKeyUsage getById(Integer id) {
        log.debug("GEKU ID: {}", id);
        return extendedKeyUsageRepository.getById(id);
    }

    public List<ExtendedKeyUsage> getAll() {
        return extendedKeyUsageRepository.findAll();
    }
}

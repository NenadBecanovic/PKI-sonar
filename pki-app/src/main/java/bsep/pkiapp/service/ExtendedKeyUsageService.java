package bsep.pkiapp.service;

import bsep.pkiapp.model.ExtendedKeyUsage;
import bsep.pkiapp.model.KeyUsage;
import bsep.pkiapp.repository.ExtendedKeyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtendedKeyUsageService {

    @Autowired
    private ExtendedKeyUsageRepository extendedKeyUsageRepository;

    public ExtendedKeyUsage getById(Integer id) { return extendedKeyUsageRepository.getById(id); }

    public List<ExtendedKeyUsage> getAll() {
        return extendedKeyUsageRepository.findAll();
    }
}

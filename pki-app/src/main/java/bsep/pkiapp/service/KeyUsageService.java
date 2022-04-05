package bsep.pkiapp.service;

import bsep.pkiapp.model.KeyUsage;
import bsep.pkiapp.repository.KeyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyUsageService {

    @Autowired
    private KeyUsageRepository keyUsageRepository;

    public KeyUsage getById(Integer id) { return keyUsageRepository.getById(id); }

}

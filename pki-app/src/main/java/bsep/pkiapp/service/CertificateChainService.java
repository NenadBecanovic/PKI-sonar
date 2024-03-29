package bsep.pkiapp.service;

import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.CertificateChainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CertificateChainService {

    @Autowired
    public CertificateChainRepository certificateChainRepository;


    public List<CertificateChain> getCAByUser(User user) {
        log.debug("GET CA U: {}", user.getEmail());
        return certificateChainRepository.getByUserAndCertificateType(user, CertificateType.INTERMEDIATE);
    }

    public List<CertificateChain> getByCertificateType(CertificateType type) {
        log.debug("Get CC by CT: {}", type.toString());
        return certificateChainRepository.getByCertificateType(type);
    }
}

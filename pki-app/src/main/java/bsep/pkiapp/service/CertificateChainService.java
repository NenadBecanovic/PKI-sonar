package bsep.pkiapp.service;

import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.CertificateChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateChainService {

    @Autowired
    public CertificateChainRepository certificateChainRepository;


    public List<CertificateChain> getCAByUser(User user) {
        return certificateChainRepository.getByUserAndCertificateType(user, CertificateType.INTERMEDIATE);
    }

    public List<CertificateChain> getByCertificateType(CertificateType type) {
        return certificateChainRepository.getByCertificateType(type);
    }
}

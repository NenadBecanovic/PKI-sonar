package bsep.pkiapp.repository;

import java.math.BigInteger;

import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bsep.pkiapp.model.CertificateChain;

import java.util.List;

@Repository
public interface CertificateChainRepository extends JpaRepository<CertificateChain, Long> {

    CertificateChain getCertificateChainBySignerSerialNumber(BigInteger signerSerialNumber);

    List<CertificateChain> getByCertificateType(CertificateType type);

    List<CertificateChain> getByUserAndCertificateType(User user, CertificateType type);

    List<CertificateChain> getByUser(User user);

    CertificateChain getBySerialNumber(BigInteger serialNumber);
}

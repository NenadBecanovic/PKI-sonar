package bsep.pkiapp.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bsep.pkiapp.model.CertificateChain;

@Repository
public interface CertificateChainRepository extends JpaRepository<CertificateChain, Long> {

	public CertificateChain getCertificateChainBySignerSerialNumber(BigInteger signerSerialNumber);

}

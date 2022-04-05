package bsep.pkiapp.repository;

import bsep.pkiapp.model.CertificateExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtensionRepository extends JpaRepository<CertificateExtension, Integer> {

}

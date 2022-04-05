package bsep.pkiapp.repository;

import bsep.pkiapp.model.ExtendedKeyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtendedKeyUsageRepository extends JpaRepository<ExtendedKeyUsage, Integer> {

}

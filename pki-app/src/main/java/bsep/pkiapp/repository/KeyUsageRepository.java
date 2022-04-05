package bsep.pkiapp.repository;

import bsep.pkiapp.model.KeyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyUsageRepository extends JpaRepository<KeyUsage, Integer> {

}

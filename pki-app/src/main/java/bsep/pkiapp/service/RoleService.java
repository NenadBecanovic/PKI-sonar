package bsep.pkiapp.service;

import bsep.pkiapp.model.Role;
import bsep.pkiapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Role getById(Integer id){
        return repository.getById(id);
    }

}

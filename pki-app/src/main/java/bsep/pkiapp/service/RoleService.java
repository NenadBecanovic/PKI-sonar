package bsep.pkiapp.service;

import bsep.pkiapp.model.Role;
import bsep.pkiapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Role getById(Integer id){
        return repository.getById(id);
    }

    public Role getByName(String name) { return repository.getByName(name); }

    public List<String> getAllPermissionsForRole(Role role) {
        return role.getPermissionNames();
    }
}

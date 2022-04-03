package bsep.pkiapp.service;

import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() { return userRepository.findAll(); }

    public User getById(Integer id) { return userRepository.getById(id); }
}

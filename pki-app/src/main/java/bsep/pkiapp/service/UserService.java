package bsep.pkiapp.service;

import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() { return userRepository.findAll(); }

    public User getById(Integer id) { return userRepository.getById(id); }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    public Boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}

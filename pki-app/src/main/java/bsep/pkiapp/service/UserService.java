package bsep.pkiapp.service;

import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.model.Role;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() { return userRepository.findAll(); }

    // TODO: possible problem, I added null because I could not compile
    public User getById(Integer id) { return userRepository.findById(id).orElseThrow(null); }

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

    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public List<UserDto> getByRole(String type) {
        List<UserDto> users = getAllAdmins();

        if(type.equals("CA")){
            users.addAll(getAllCAs());
        }else{
            users.addAll(getAllEndEntities());
        }
        return users;
    }

    private List<UserDto> getAllEndEntities() {
        List<UserDto> endEntities = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole().getAuthority().equals("ROLE_USER")) {
                endEntities.add(new UserDto(user));
            }
        }
        return endEntities;
    }

    private List<UserDto> getAllCAs() {
        List<UserDto> CAs = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole().getAuthority().equals("ROLE_CA")) {
                CAs.add(new UserDto(user));
            }
        }
        return CAs;
    }

    private List<UserDto> getAllAdmins() {
        List<UserDto> admins = new ArrayList<>();
        for(User user: userRepository.findAll()){
            if(user.getRole().getAuthority().equals("ROLE_ADMIN")){
                admins.add(new UserDto(user));
            }
        }
        return admins;
    }

    public List<UserDto> getAllSubjects() {
        List<UserDto> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (!user.getRole().getAuthority().equals("ROLE_ADMIN")) {
                users.add(new UserDto(user));
            }
        }
        return users;
    }
}

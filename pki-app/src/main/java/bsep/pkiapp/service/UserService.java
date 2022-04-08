package bsep.pkiapp.service;

import bsep.pkiapp.dto.IssuerDto;
import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.Role;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.UserRepository;
import bsep.pkiapp.security.util.TokenUtils;
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

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CertificateChainService certificateChainService;

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

    public List<IssuerDto> getByRole(String token, String type) {
        String email = tokenUtils.getEmailFromToken(token);
        User user = getByEmail(email);

        List<IssuerDto> issuers;

        if(user.getRole().getAuthority().equals("ROLE_ADMIN")){
            issuers = getIssuersForAdmin(type);
        }else{
            issuers = getIssuersForCA(user);
        }
//        List<User> users = new ArrayList<>();
//        List<CertificateChain> certificates = new ArrayList<>();
//        if(user.getRole().getAuthority().equals("ROLE_ADMIN")){
//            users = getAllAdmins();
//            if (!type.equals("ROOT")) {
//                users.addAll(getAllCAs());
//            }
//        }else if(user.getRole().getAuthority().equals("ROLE_CA")){
//            certificates = certificateChainService.getCAByUser(user);
//        }
//        return users.size() > 0 ? convertUsertToIssuerDto(users) : convertCerificateToIssuerDto(certificates);
        return issuers;
    }

    private List<IssuerDto> getIssuersForCA(User user) {
        List<CertificateChain> certificates = certificateChainService.getCAByUser(user);
        return convertCerificateToIssuerDto(certificates);
    }

    private List<IssuerDto> getIssuersForAdmin(String type) {
        List<User> users = new ArrayList<>();
        List<CertificateChain> certificates = new ArrayList<>();
        if(type.equals("ROOT")){
            users = getAllAdmins();
        }else{
            certificates = certificateChainService.getByCertificateType(CertificateType.ROOT);
            certificates.addAll(certificateChainService.getByCertificateType(CertificateType.INTERMEDIATE));
        }
        return users.size() > 0 ? convertUsertToIssuerDto(users) : convertCerificateToIssuerDto(certificates);
    }

    private List<IssuerDto> convertCerificateToIssuerDto(List<CertificateChain> certificates) {
        List<IssuerDto> dtos = new ArrayList<>();
        for(CertificateChain cert: certificates){
            dtos.add(new IssuerDto(cert));
        }
        return  dtos;
    }

    private List<IssuerDto> convertUsertToIssuerDto(List<User> users) {
        List<IssuerDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(new IssuerDto(user));
        }
        return dtos;
    }

    private List<User> getAllEndEntities() {
        List<User> endEntities = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole().getAuthority().equals("ROLE_USER")) {
                endEntities.add(user);
            }
        }
        return endEntities;
    }

    private List<User> getAllCAs() {
        List<User> CAs = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole().getAuthority().equals("ROLE_CA")) {
                CAs.add(user);
            }
        }
        return CAs;
    }

    private List<User> getAllAdmins() {
        List<User> admins = new ArrayList<>();
        for(User user: userRepository.findAll()){
            if(user.getRole().getAuthority().equals("ROLE_ADMIN")){
                admins.add(user);
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

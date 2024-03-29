package bsep.pkiapp.service;

import bsep.pkiapp.dto.IssuerDto;
import bsep.pkiapp.dto.SubjectDto;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.UserRepository;
import bsep.pkiapp.security.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CertificateChainService certificateChainService;

    public List<User> getAll() { return userRepository.findAll(); }

    public User getById(Integer id) { return userRepository.findById(id).orElseThrow(null); }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("LU: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("FAILED LU: {}, NOT FOUND", email);
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    public Boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void saveUser(User user) {
        log.debug("SU: {}", user.getEmail());
        userRepository.save(user);
    }

    public User getByEmail(String email) {
        log.debug("GUE: {}", email);
        return userRepository.getByEmail(email);
    }

    public List<IssuerDto> getByRole(String token, String type) {
        log.debug("GI by UR");
        String email = tokenUtils.getEmailFromToken(token);
        User user = getByEmail(email);

        List<IssuerDto> issuers;

        if(user.getRole().getAuthority().equals("ROLE_ADMIN")){
            issuers = getIssuersForAdmin(type);
        }else{
            issuers = getIssuersForCA(user);
        }
        return issuers;
    }

    private List<IssuerDto> getIssuersForCA(User user) {
        log.debug("GI for CA by U: {}", user.getEmail());
        List<CertificateChain> certificates = certificateChainService.getCAByUser(user);
        return convertCerificateToIssuerDto(certificates);
    }

    private List<IssuerDto> getIssuersForAdmin(String type) {
        log.debug("GI for A with CT: {}", type);
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
        log.debug("CC to IL");
        List<IssuerDto> dtos = new ArrayList<>();
        for(CertificateChain cert: certificates){
            dtos.add(new IssuerDto(cert));
        }
        return  dtos;
    }

    private List<IssuerDto> convertUsertToIssuerDto(List<User> users) {
        log.debug("CU to IL");
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

    public List<SubjectDto> getAllSubjects() {
        List<SubjectDto> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (!user.getRole().getAuthority().equals("ROLE_ADMIN")) {
                users.add(new SubjectDto(user));
            }
        }
        return users;
    }

    public User getUserFromToken(String token) {
        String email = tokenUtils.getEmailFromToken(token);
        return userRepository.getByEmail(email);
    }
}

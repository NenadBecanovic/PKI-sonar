package bsep.pkiapp.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    String name;

    @Override
    public String getAuthority() {
        return name;
    }
}

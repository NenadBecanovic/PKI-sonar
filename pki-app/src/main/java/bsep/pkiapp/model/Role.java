package bsep.pkiapp.model;

import javax.persistence.*;

@Entity
public class Role {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    String name;
}

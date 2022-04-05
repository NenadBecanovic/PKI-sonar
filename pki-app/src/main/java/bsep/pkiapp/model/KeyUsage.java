package bsep.pkiapp.model;

import javax.persistence.*;

@Entity
public class KeyUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="keyUsageType")
    private KeyUsageType keyUsageType;

    public KeyUsage() {}

    public KeyUsage(Integer id, String name, KeyUsageType keyUsageType) {
        this.id = id;
        this.name = name;
        this.keyUsageType = keyUsageType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyUsageType getKeyUsageType() {
        return keyUsageType;
    }

    public void setKeyUsageType(KeyUsageType keyUsageType) {
        this.keyUsageType = keyUsageType;
    }
}

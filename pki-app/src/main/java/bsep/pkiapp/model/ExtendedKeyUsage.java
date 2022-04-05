package bsep.pkiapp.model;

import javax.persistence.*;

@Entity
public class ExtendedKeyUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="keyUsageType")
    private ExtendedKeyUsageType extendedKeyUsageType;

    public ExtendedKeyUsage() {}

    public ExtendedKeyUsage(Integer id, String name, ExtendedKeyUsageType extendedKeyUsageType) {
        this.id = id;
        this.name = name;
        this.extendedKeyUsageType = extendedKeyUsageType;
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

    public ExtendedKeyUsageType getExtendedKeyUsageType() {
        return extendedKeyUsageType;
    }

    public void setExtendedKeyUsageType(ExtendedKeyUsageType extendedKeyUsageType) {
        this.extendedKeyUsageType = extendedKeyUsageType;
    }
}

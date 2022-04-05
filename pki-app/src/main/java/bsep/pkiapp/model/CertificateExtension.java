package bsep.pkiapp.model;

import javax.persistence.*;

@Entity
public class CertificateExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ExtensionType extensionType;

    public CertificateExtension() {}

    public CertificateExtension(Integer id, String name, ExtensionType extensionType) {
        this.id = id;
        this.name = name;
        this.extensionType = extensionType;
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

    public ExtensionType getExtensionType() {
        return extensionType;
    }

    public void setExtensionType(ExtensionType extensionType) {
        this.extensionType = extensionType;
    }
}

package bsep.pkiapp.dto;

import org.bouncycastle.asn1.x500.X500Name;

import java.security.PublicKey;
import java.util.List;

public class ExtensionSettingsDto {

    public List<Integer> extensionsIds;

    public List<Integer> keyUsageIds;

    public List<Integer> extendedKeyUsageIds;

    public PublicKey publicKey;

    public X500Name subjectName;

    public ExtensionSettingsDto() {}

    public ExtensionSettingsDto(List<Integer> extensionsIds, List<Integer> keyUsageIds, List<Integer> extendedKeyUsageIds) {
        this.extensionsIds = extensionsIds;
        this.keyUsageIds = keyUsageIds;
        this.extendedKeyUsageIds = extendedKeyUsageIds;
    }

    public List<Integer> getExtensionsIds() {
        return extensionsIds;
    }

    public void setExtensionsIds(List<Integer> extensionsIds) {
        this.extensionsIds = extensionsIds;
    }

    public List<Integer> getKeyUsageIds() {
        return keyUsageIds;
    }

    public void setKeyUsageIds(List<Integer> keyUsageIds) {
        this.keyUsageIds = keyUsageIds;
    }

    public List<Integer> getExtendedKeyUsageIds() {
        return extendedKeyUsageIds;
    }

    public void setExtendedKeyUsageIds(List<Integer> extendedKeyUsageIds) {
        this.extendedKeyUsageIds = extendedKeyUsageIds;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public X500Name getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(X500Name subjectName) {
        this.subjectName = subjectName;
    }
}

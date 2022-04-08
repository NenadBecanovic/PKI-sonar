package bsep.pkiapp.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;


@Entity
public class CertificateChain {

	@Id
	@SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq", initialValue = 1)
    @GeneratedValue(generator = "mySeqGen")
	private Long id;
	
	private BigInteger serialNumber;
	
	private BigInteger signerSerialNumber;
	
	private boolean revoked;
	
	private String commonName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	private Date dateFrom;
	
	private Date dateTo;

    @Enumerated(EnumType.STRING)
	@Column(name="certificateType")
	private CertificateType certificateType;
    
    private boolean hasSigningPermission;

	public CertificateChain() {}
	
	
	public CertificateChain(BigInteger signerSerialNumber, String commonName, CertificateType certificateType, User user, Date dateFrom, Date dateTo, boolean hasSigningPermission) {
		super();
		this.signerSerialNumber = signerSerialNumber;
		this.revoked = false;
		this.commonName = commonName;
		this.certificateType = certificateType;
		this.user=user;
		this.dateFrom=dateFrom;
		this.dateTo=dateTo;
		this.hasSigningPermission = hasSigningPermission;
	}	

	public CertificateChain(BigInteger serialNumber, BigInteger signerSerialNumber, boolean revoked, String commonName, CertificateType certificateType, boolean hasSigningPermission) {
		super();
		this.serialNumber = serialNumber;
		this.signerSerialNumber = signerSerialNumber;
		this.revoked = revoked;
		this.commonName = commonName;
		this.certificateType = certificateType;
		this.hasSigningPermission = hasSigningPermission;
	}

	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}

	public BigInteger getSignerSerialNumber() {
		return signerSerialNumber;
	}

	public void setSignerSerialNumber(BigInteger signerSerialNumber) {
		this.signerSerialNumber = signerSerialNumber;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}


	public boolean isHasSigningPermission() {
		return hasSigningPermission;
	}


	public void setHasSigningPermission(boolean hasSigningPermission) {
		this.hasSigningPermission = hasSigningPermission;
	}

	
}


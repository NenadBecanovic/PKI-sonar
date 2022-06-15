package bsep.pkiapp.security.util;

import lombok.ToString;

@ToString
public class TfaAuthenticationRequest {
    private String email;
    private String password;
    private String code;

    public TfaAuthenticationRequest() {
        super();
    }

    public TfaAuthenticationRequest(String email, String password, String code) {
        super();
        this.email = email;
        this.password = password;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }
}


package bsep.pkiapp.dto;

public class ForgottenPasswordDto {

    private String newPassword;

    private String newPasswordRetyped;

    public ForgottenPasswordDto() {
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRetyped() {
        return newPasswordRetyped;
    }

    public void setNewPasswordRetyped(String newPasswordRetyped) {
        this.newPasswordRetyped = newPasswordRetyped;
    }
}

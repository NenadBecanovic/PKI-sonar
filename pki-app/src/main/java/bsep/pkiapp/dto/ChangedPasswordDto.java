package bsep.pkiapp.dto;

public class ChangedPasswordDto {

    private String newPassword;

    private String newPasswordRetyped;

    private String oldPassword;

    public ChangedPasswordDto() {
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

    public String getOldPassword() { return oldPassword; }

    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
}

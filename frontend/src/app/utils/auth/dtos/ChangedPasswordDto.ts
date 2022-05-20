export class ChangedPasswordDto {
    private oldPassword: string;
    private newPassword: string;
    private newPasswordRetyped: string;

    constructor(oldPassword: string, newPassword: string, newPasswordRetyped: string) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordRetyped = newPasswordRetyped;
    }

}